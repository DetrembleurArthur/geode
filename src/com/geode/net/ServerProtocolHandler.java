package com.geode.net;

import com.geode.annotations.Control;
import com.geode.annotations.Protocol;
import com.geode.net.Query.Category;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerProtocolHandler extends ProtocolHandler
{
    private static final Logger logger = Logger.getLogger(ServerProtocolHandler.class);
    private static AtomicInteger counter = new AtomicInteger(0);
    private ArrayList<Class<?>> protocolClasses;
    private final Server server;
    private final ArrayList<Queue> subscribedQueues;
    private final Thread queueConsumerDameon;
    
    public ServerProtocolHandler(Socket socket, Server server)
    {
        super(socket);
        this.protocolClasses = server.getServerInfos().getProtocolClasses();
        this.server = server;
        subscribedQueues = new ArrayList<>();
        queueConsumerDameon = new Thread(this::consumeDaemon);
        queueConsumerDameon.setDaemon(true);
    }

    @Override
    protected Object discovery()
    {
        try
        {
            tunnel.send(Query.simple("protocol").setCategory(Query.Category.DISCOVERY));
            Query query = tunnel.recv();
            if(query.getType().equals("protocol_send"))
            {
                if(query.getCategory() == Category.DISCOVERY)
                {
                    if(query.getArgs().size() == 1)
                    {
                        String protocolName = (String)query.getArgs().get(0);
                        for(Class<?> protocolClass : protocolClasses)
                        {
                            String name = protocolClass.getAnnotation(Protocol.class).value();
                            if(name.equalsIgnoreCase(protocolName))
                            {
                                identifier = counter.incrementAndGet();
                                tunnel.send(Query.simple("protocol_ok").setCategory(Query.Category.DISCOVERY).pack(identifier));
                                protocolClasses = null;
                                return protocolClass.getDeclaredConstructor().newInstance();
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            logger.fatal("protocol discovery failed: " + e.getMessage());
            gState = GState.BROKEN;
        }
        try
        {
			tunnel.send(Query.simple("protocol_err"));
		} catch (IOException e)
        {
			logger.fatal("fatal error: " + e.getMessage());
            gState = GState.BROKEN;
		}
        return null;
    }

    @Override
    protected boolean testControl(Control control)
    {
        return control.type() == Control.Type.CLASSIC;
    }

    @Override
	protected Serializable manageTopicNotifyQuery(Query query)
	{
		server.notifySubscribers(query);
		return null;
	}

    @Override
    protected Serializable manageTopicNotifyOthersQuery(Query query)
    {
        server.notifyOtherSubscribers(query, this);
        return null;
    }

	@Override
    protected Serializable manageTopicSubscribeQuery(Query query)
	{
		server.subscribeTopic(query.getType(), this);
		return null;
	}

    @Override
    protected Serializable manageTopicUnsubscribeQuery(Query query)
    {
        server.unsubscribeTopic(query.getType(), this);
        return null;
    }

    @Override
    protected Serializable manageNotifyQuery(Query query)
    {
        server.notifyOther(query, this);
        return null;
    }

    @Override
    protected Object manageQueueSubscribeQuery(Query query)
    {
        server.subscribeQueue(query.getType(), this);
        return null;
    }

    @Override
    protected Object manageQueueUnsubscribeQuery(Query query)
    {
        server.unsubscribeQueue(query.getType(), this);
        return null;
    }

    @Override
    protected Object manageQueueProduceQuery(Query query)
    {
        server.produceQueue(query, this);
        return null;
    }

    public synchronized void subscribeQueue(Queue queue)
    {
        subscribedQueues.add(queue);
        if(!queueConsumerDameon.isAlive())
        {
            queueConsumerDameon.start();
        }
    }

    public synchronized void unsubscribeQueue(Queue queue)
    {
        subscribedQueues.remove(queue);
        if(queueConsumerDameon.isAlive())
        {
            queueConsumerDameon.interrupt();
        }
    }

    public synchronized void queueProduce(Queue queue, Query query)
    {
        synchronized (queue)
        {
            queue.produce(query);
        }
    }

    private void consumeDaemon()
    {
        logger.info("start consumer daemon");
        try
        {
            while(true)
            {
                if(queueConsumerDameon.isInterrupted())
                {
                    logger.warn("consume dameon interrupted...");
                    return;
                }
                synchronized (this)
                {
                    for(Queue queue : subscribedQueues)
                    {
                        synchronized (queue)
                        {
                            Query query = queue.consume();
                            if(query != null)
                            {
                                send(query);
                            }
                        }
                    }
                }
                Thread.sleep(10);
            }
        }
        catch (Exception e)
        {
            logger.error("consume daemon error: " + e.getMessage());
            logger.warn("consume dameon interrupted...");
        }
    }

    @Override
    protected void end()
    {
        try
        {
            server.remove(this);
            tunnel.getSocket().close();
            queueConsumerDameon.interrupt();
            logger.warn("Protocol server handler is closed...");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
