package com.geode.net;

import java.net.Socket;

import org.apache.log4j.Logger;

import com.geode.annotations.Protocol;

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
}
