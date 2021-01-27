package com.geode.net;

import java.io.Serializable;
import java.net.Socket;

import com.geode.annotations.Control;
import org.apache.log4j.Logger;

import com.geode.annotations.Protocol;
import com.geode.net.Q.Category;

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
            Q query = tunnel.recv();
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
	protected Serializable manageTopicNotifyQuery(Q query)
	{
		return manageControlQuery(query, Control.Type.CLIENT_CLIENTS);
	}

    @Override
    protected Serializable manageTopicNotifyOthersQuery(Q query)
    {
        return manageTopicNotifyQuery(query);
    }
	
	public void subscribe(String topic)
	{
		send(new Q(topic).setCategory(Category.TOPIC_SUBSCRIBE));
	}
    public void unsubscribe(String topic)
    {
        send(new Q(topic).setCategory(Category.TOPIC_UNSUBSCRIBE));
    }
}
