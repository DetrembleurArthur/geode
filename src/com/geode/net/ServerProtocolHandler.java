package com.geode.net;

import com.geode.annotations.Protocol;
import com.geode.net.Q.Category;

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
    private Server server;
    
    public ServerProtocolHandler(Socket socket, Server server)
    {
        super(socket);
        this.protocolClasses = server.getServerInfos().getProtocolClasses();
        this.server = server;
    }

    @Override
    protected Object discovery()
    {
        try
        {
            tunnel.send(Q.simple("protocol").setCategory(Q.Category.DISCOVERY));
            Q query = tunnel.recv();
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
                                tunnel.send(Q.simple("protocol_ok").setCategory(Q.Category.DISCOVERY).pack(identifier));
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
			tunnel.send(Q.simple("protocol_err"));
		} catch (IOException e)
        {
			logger.fatal("fatal error: " + e.getMessage());
            gState = GState.BROKEN;
		}
        return null;
    }
    
    @Override
    protected Serializable manageQuery(Q query)
    {
        switch (query.getCategory())
        {
            case NORMAL:
                return manageNormalQuery(query);
            case TOPIC_SUBSCRIBE:
            	return manageTopicSubscribeQuery(query);
            case TOPIC_NOTIFY:
            	return manageTopicNotifyQuery(query);
            default:
                logger.warn(query.getCategory() + " are not allowed here");
        }
        return null;
    }

	private Serializable manageTopicNotifyQuery(Q query)
	{
		server.notifySubscribers(query);
		return null;
	}

	private Serializable manageTopicSubscribeQuery(Q query)
	{
		server.subscribe(query.getType(), this);
		return null;
	}
}
