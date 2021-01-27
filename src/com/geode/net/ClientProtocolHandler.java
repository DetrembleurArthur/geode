package com.geode.net;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import com.geode.annotations.Control;
import org.apache.log4j.Logger;

import com.geode.annotations.Protocol;
import com.geode.net.Query.Category;

public class ClientProtocolHandler extends ProtocolHandler
{
    private static final Logger logger = Logger.getLogger(ClientProtocolHandler.class);
    private Class<?> protocolClass;
    
    public ClientProtocolHandler(Socket socket, Class<?> protocolClass)
    {
        super(socket);
        this.protocolClass = protocolClass;
    }

    @Override
    protected Object discovery()
    {
        try
        {
            Query query = tunnel.recv();
            if(query.getType().equalsIgnoreCase("protocol"))
            {
            	query.setType("protocol_send");
            	query.pack(protocolClass.getAnnotation(Protocol.class).value());
            	tunnel.send(query);
            	query = tunnel.recv();
            	if(query.getType().equalsIgnoreCase("protocol_ok"))
            	{
            		logger.info("protocol discovery success");
            		identifier = (int) query.getArgs().get(0);
            		return protocolClass.getConstructor().newInstance();
            	}
            }
        }
        catch(Exception e)
        {
            logger.fatal("protocol discovery failed: " + e.getMessage());
            gState = GState.BROKEN;
        }
        logger.fatal("protocol discovery failed");
        return null;
    }

    @Override
	protected Serializable manageTopicNotifyQuery(Query query)
	{
		return manageControlQuery(query, Control.Type.TOPIC);
	}

    @Override
    protected Serializable manageTopicNotifyOthersQuery(Query query)
    {
        return manageTopicNotifyQuery(query);
    }

    @Override
    protected Serializable manageNotifyQuery(Query query)
    {
        return manageControlQuery(query, Control.Type.DIRECT);
    }

    @Override
    protected Object manageQueueConsumeQuery(Query query)
    {
        return manageControlQuery(query, Control.Type.QUEUE);
    }

    public void subscribeTopic(String topic)
	{
		send(new Query(topic).setCategory(Category.TOPIC_SUBSCRIBE));
	}
    public void unsubscribeTopic(String topic)
    {
        send(new Query(topic).setCategory(Category.TOPIC_UNSUBSCRIBE));
    }

    public void subscribeQueue(String queue)
    {
        send(new Query(queue).setCategory(Category.QUEUE_SUBSCRIBE));
    }
    public void unsubscribeQueue(String queue)
    {
        send(new Query(queue).setCategory(Category.QUEUE_UNSUBSCRIBE));
    }

    @Override
    protected void end()
    {
        try
        {
            tunnel.getSocket().close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
