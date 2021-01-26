package com.geode.net;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.geode.annotations.Protocol;
import com.geode.net.Q.Category;

public class ClientProtocolHandler extends ProtocolHandler
{
    private static final Logger logger = Logger.getLogger(ClientProtocolHandler.class);
    private Class<?> protocolClass;
    private final HashMap<String, TopicListener> topicListeners;
    
    public ClientProtocolHandler(Socket socket, Class<?> protocolClass)
    {
        super(socket);
        this.protocolClass = protocolClass;
        topicListeners = new HashMap<>();
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
    protected Serializable manageQuery(Q query)
    {
        switch (query.getCategory())
        {
            case NORMAL:
                return manageNormalQuery(query);
            case TOPIC_NOTIFY:
            	return manageTopicNotifyQuery(query);
            default:
                logger.warn(query.getCategory() + " are not allowed here");
        }
        return null;
    }

	private Serializable manageTopicNotifyQuery(Q query)
	{
		TopicListener runnable = topicListeners.getOrDefault(query.getType(), null);
		if(runnable != null)
			runnable.trigger(query.getArgs());
		return null;
	}
	
	public void subscribe(String topic, TopicListener listener)
	{
		topicListeners.put(topic, listener);
		send(new Q(topic).setCategory(Category.TOPIC_SUBSCRIBE));
	}
}
