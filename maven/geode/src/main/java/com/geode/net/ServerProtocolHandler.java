package com.geode.net;

import com.geode.annotations.Control;
import com.geode.annotations.Protocol;
import com.geode.logging.Logger;
import com.geode.net.GeodeQuery.Category;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Server protocol handler.
 */
public class ServerProtocolHandler extends ProtocolHandler
{
    private static final Logger logger = new Logger(ServerProtocolHandler.class);
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final Server server;
    private final ArrayList<Queue> subscribedQueues;
    private final Thread queueConsumerDameon;
    private ArrayList<Class<?>> protocolClasses;

    /**
     * Instantiates a new Server protocol handler.
     *
     * @param socket the socket
     * @param server the server
     */
    public ServerProtocolHandler(Socket socket, Server server, boolean discovery)
    {
        super(socket, discovery);
        this.protocolClasses = server.getServerInfos().getProtocolClasses();
        this.server = server;
        subscribedQueues = new ArrayList<>();
        setDaemon(true);
        queueConsumerDameon = new Thread(this::consumeDaemon);
        queueConsumerDameon.setDaemon(true);
    }

    @Override
    protected Object discovery()
    {
        logger.info("start discovery protocol");
        try
        {
            tunnel.send(GeodeQuery.simple("protocol").setCategory(GeodeQuery.Category.DISCOVERY));
            GeodeQuery geodeQuery = tunnel.recv();
            if (geodeQuery.getType().equals("protocol_send"))
            {
                if (geodeQuery.getCategory() == Category.DISCOVERY)
                {
                    if (geodeQuery.getArgs().size() == 1)
                    {
                        String protocolName = (String) geodeQuery.getArgs().get(0);
                        for (Class<?> protocolClass : protocolClasses)
                        {
                            String name = protocolClass.getAnnotation(Protocol.class).value();
                            System.out.println(protocolName + " " + name);
                            if (name.equalsIgnoreCase(protocolName))
                            {
                                identifier = counter.incrementAndGet();
                                tunnel.send(GeodeQuery.simple("protocol_ok").setCategory(GeodeQuery.Category.DISCOVERY).pack(identifier));
                                protocolClasses = null;
                                return protocolClass.getDeclaredConstructor().newInstance();
                            }
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            logger.fatal("protocol discovery failed: " + e.getMessage());
            gState = GState.BROKEN;
        }
        try
        {
            tunnel.send(GeodeQuery.simple("protocol_err"));
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
    protected Serializable manageTopicNotifyQuery(GeodeQuery geodeQuery)
    {
        server.topicNotifySubscribers(geodeQuery, this);
        return null;
    }

    @Override
    protected Serializable manageTopicNotifyOthersQuery(GeodeQuery geodeQuery)
    {
        server.topicNotifyOtherSubscribers(geodeQuery, this);
        return null;
    }

    @Override
    protected Serializable manageTopicSubscribeQuery(GeodeQuery geodeQuery)
    {
        server.subscribeTopic(geodeQuery.getType(), this);
        return null;
    }

    @Override
    protected Serializable manageTopicUnsubscribeQuery(GeodeQuery geodeQuery)
    {
        server.unsubscribeTopic(geodeQuery.getType(), this);
        return null;
    }

    @Override
    protected Serializable manageNotifyQuery(GeodeQuery geodeQuery)
    {
        server.notifyOther(geodeQuery, this);
        return null;
    }

    @Override
    protected Object manageQueueSubscribeQuery(GeodeQuery geodeQuery)
    {
        server.subscribeQueue(geodeQuery.getType(), this);
        return null;
    }

    @Override
    protected Object manageQueueUnsubscribeQuery(GeodeQuery geodeQuery)
    {
        server.unsubscribeQueue(geodeQuery.getType(), this);
        return null;
    }

    @Override
    protected Object manageQueueProduceQuery(GeodeQuery geodeQuery)
    {
        server.produceQueue(geodeQuery, this);
        return null;
    }

    /**
     * Subscribe queue.
     *
     * @param queue the queue
     */
    public synchronized void subscribeQueue(Queue queue)
    {
        subscribedQueues.add(queue);
        if (!queueConsumerDameon.isAlive())
        {
            queueConsumerDameon.start();
        }
    }

    /**
     * Unsubscribe queue.
     *
     * @param queue the queue
     */
    public synchronized void unsubscribeQueue(Queue queue)
    {
        subscribedQueues.remove(queue);
        if (queueConsumerDameon.isAlive())
        {
            queueConsumerDameon.interrupt();
        }
    }

    /**
     * Queue produce.
     *
     * @param queue the queue
     * @param geodeQuery the query
     */
    public synchronized void queueProduce(Queue queue, GeodeQuery geodeQuery)
    {
        synchronized (queue)
        {
            queue.produce(geodeQuery);
        }
    }

    private void consumeDaemon()
    {
        logger.info("start consumer daemon");
        try
        {
            while (true)
            {
                if (queueConsumerDameon.isInterrupted())
                {
                    logger.warning("consume dameon interrupted...");
                    return;
                }
                synchronized (this)
                {
                    for (Queue queue : subscribedQueues)
                    {
                        synchronized (queue)
                        {
                            GeodeQuery geodeQuery = queue.consume();
                            if (geodeQuery != null)
                            {
                                send(geodeQuery);
                            }
                        }
                    }
                }
                sleep(10);
            }
        } catch (Exception e)
        {
            logger.error("consume daemon error: " + e.getMessage());
            logger.warning("consume dameon interrupted...");
        }
    }

    @Override
    public void end()
    {
        try
        {
            logger.debug("ending ***");
            server.remove(this);
            tunnel.getSocket().close();
            queueConsumerDameon.interrupt();
            logger.warning("Protocol server handler is closed...");
        } catch (IOException e)
        {
            e.printStackTrace();
            logger.error("end handler : " + e.getMessage());
        }
    }

    @Override
    protected Object createProtocol()
    {
        try {
            return protocolClasses.get(0).getConstructor().newInstance();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
