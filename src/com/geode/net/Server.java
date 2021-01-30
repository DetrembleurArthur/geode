package com.geode.net;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server extends Thread implements Initializable
{
    private static final Logger logger = Logger.getLogger(Server.class);
    private ServerSocket serverSocket;
    private final CopyOnWriteArrayList<ProtocolHandler> handlers;
    private GState gState;
    private final ServerInfos serverInfos;
    private final HashMap<String, ArrayList<ProtocolHandler>> topicsMap;
    private final HashMap<String, Queue> queuesMap;

    public Server(ServerInfos serverInfos)
    {
        this.serverInfos = serverInfos;
        gState = GState.DOWN;
        handlers = new CopyOnWriteArrayList<>();
        topicsMap = new HashMap<>();
        queuesMap = new HashMap<>();
    }

    @Override
    public void init()
    {
        try
        {
            serverSocket = new ServerSocket(serverInfos.getPort(), serverInfos.getBacklog(), InetAddress.getByName(serverInfos.getHost()));
            gState = GState.READY;
        } catch (IOException e)
        {
            logger.fatal("unable to initialise the server: " + serverInfos + " -> " + e.getMessage());
            gState = GState.BROKEN;
        }
        logger.info("init server: " + serverInfos);
    }

    @Override
    public void run()
    {
    	init();
        if(gState == GState.READY)
        {
            logger.info("server is running");
            gState = GState.RUNNING;
            while(gState == GState.RUNNING)
            {
                try
                {
                    Socket socket = serverSocket.accept();
                    logger.info("client connection accepted: " + socket);
                    ServerProtocolHandler handler = new ServerProtocolHandler(socket, this);
                    handler.start();
                    handlers.add(handler);
                } catch (IOException e)
                {
                    logger.error("server accept error: " + e.getMessage());
                }
            }
        }
        else
        {
            logger.fatal("server " + gState + " can not run");
        }
    }

    public void registerProtocolClass(String ... classNames)
    {
        for(String className : classNames)
        {
            try
            {
                serverInfos.getProtocolClasses().add(Class.forName(className));
                logger.info("register protocolClass: " + className);
            } catch (ClassNotFoundException e)
            {
                logger.error("unable to register protocolClass: " + className +" -> " + e.getMessage());
            }
        }
    }

    public void registerProtocolClass(Class<?> ... classes)
    {
        for(Class<?> pclass : classes)
        {
            serverInfos.getProtocolClasses().add(pclass);
            logger.info("register protocolClass: " + pclass);
        }
    }

    public ServerSocket getServerSocket()
    {
        return serverSocket;
    }

    public ArrayList<Class<?>> getProtocolClasses()
    {
        return serverInfos.getProtocolClasses();
    }

    public GState getGState()
    {
        return gState;
    }

    public ServerInfos getServerInfos()
    {
        return serverInfos;
    }
    
    public synchronized void subscribeTopic(String topic, ProtocolHandler handler)
    {
    	ArrayList<ProtocolHandler> handlers;
    	if(!topicsMap.containsKey(topic))
    	{
    		handlers = new ArrayList<>();
    		topicsMap.put(topic, handlers);
    	}
    	else
    	{
    		handlers = topicsMap.get(topic);
    	}
    	if(!handlers.contains(handler))
    	{
    		handlers.add(handler);
    	}
    }

    public synchronized void unsubscribeTopic(String topic, ProtocolHandler handler)
    {
        if(topicsMap.containsKey(topic))
        {
            topicsMap.get(topic).remove(handler);
        }
    }

    private void unsubscribeTopic(ServerProtocolHandler serverProtocolHandler)
    {
        for(String key : topicsMap.keySet())
        {
            topicsMap.get(key).remove(serverProtocolHandler);
        }
    }
    
    public synchronized void notifySubscribers(Query query)
    {
    	String topic = query.getType();
    	ArrayList<ProtocolHandler> handlers = topicsMap.get(topic);
    	for(ProtocolHandler handler : handlers)
    	{
    		handler.send(query);
    	}
    }

    public synchronized void notifyOtherSubscribers(Query query, ServerProtocolHandler serverProtocolHandler)
    {
        String topic = query.getType();
        ArrayList<ProtocolHandler> handlers = topicsMap.get(topic);
        for(ProtocolHandler handler : handlers)
        {
            if(handler != serverProtocolHandler)
                handler.send(query);
        }
    }

    public synchronized void notifyOther(Query query, ServerProtocolHandler serverProtocolHandler)
    {
        ArrayList<Integer> ids = (ArrayList<Integer>) query.getArgs().get(0);
        query.getArgs().set(0, serverProtocolHandler.getIdentifier());
        for(ProtocolHandler handler : handlers)
        {
            if(ids.contains(handler.getIdentifier()))
            {
                handler.send(query);
            }
        }
    }

    public synchronized void remove(ServerProtocolHandler serverProtocolHandler)
    {
        handlers.remove(serverProtocolHandler);
        unsubscribeTopic(serverProtocolHandler);
    }

    public void subscribeQueue(String type, ServerProtocolHandler serverProtocolHandler)
    {
        Queue queue = queuesMap.getOrDefault(type, null);
        if(queue == null)
        {
            queue = new Queue();
            queuesMap.put(type, queue);
        }
        serverProtocolHandler.subscribeQueue(queue);
    }

    public void unsubscribeQueue(String type, ServerProtocolHandler serverProtocolHandler)
    {
        Queue queue = queuesMap.getOrDefault(type, null);
        if(queue != null)
        {
            serverProtocolHandler.unsubscribeQueue(queue);
        }
    }

    public void produceQueue(Query query, ServerProtocolHandler serverProtocolHandler)
    {
        Queue queue = queuesMap.getOrDefault(query.getType(), null);
        if(queue != null)
        {
            serverProtocolHandler.queueProduce(queue, query.setCategory(Query.Category.QUEUE_CONSUME));
        }
    }
}
