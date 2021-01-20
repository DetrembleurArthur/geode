package com.geode.net;

import com.geode.annotations.Control;
import com.geode.annotations.Protocol;
import com.geode.net.Q.Category;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;

public class ServerProtocolHandler extends ProtocolHandler
{
    private static final Logger logger = Logger.getLogger(ServerProtocolHandler.class);
    private ArrayList<Class<?>> protocolClasses = new ArrayList<>();
    
    public ServerProtocolHandler(Socket socket, ArrayList<Class<?>> protocolClasses)
    {
        super(socket);
        this.protocolClasses = protocolClasses;
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
                            String name = protocolClass.getAnnotation(Protocol.class).name();
                            if(name.equalsIgnoreCase(protocolName))
                            {
                                tunnel.send(Q.simple("protocol_ok").setCategory(Q.Category.DISCOVERY));
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
}
