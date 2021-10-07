package com.geode.net;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import com.geode.net.tls.TLSUtils;

/**
 * The type Server.
 */
public class Server extends Thread implements Initializable
{
    private static final Logger logger = LogManager.getLogger(Server.class);
    private final CopyOnWriteArrayList<ProtocolHandler> handlers;
    private final ServerInfos serverInfos;
    private final HashMap<String, ArrayList<ProtocolHandler>> topicsMap;
    private final HashMap<String, Queue> queuesMap;
    private ServerSocket serverSocket;
    private GState gState;

    /**
     * Instantiates a new Server.
     *
     * @param serverInfos the server infos
     */
    public Server(ServerInfos serverInfos)
    {
        this.serverInfos = serverInfos;
        gState = GState.DOWN;
        handlers = new CopyOnWriteArrayList<>();
        topicsMap = new HashMap<>();
        queuesMap = new HashMap<>();
    }

    public ServerSocket initServerSocket() throws Exception
    {
        if(serverInfos.isTLSEnable())
        {
            ServerSocketFactory factory = TLSUtils.getServerSocketFactory(
                serverInfos.getCafile(),
                serverInfos.getCertfile(),
                serverInfos.getKeyfile()
            );
            return factory.createServerSocket(
                serverInfos.getPort(),
                serverInfos.getBacklog(),
                InetAddress.getByName(serverInfos.getHost())
            );
        }
        return new ServerSocket(serverInfos.getPort(), serverInfos.getBacklog(), InetAddress.getByName(serverInfos.getHost()));
    }

    @Override
    public void init()
    {
        try
        {
            serverSocket = initServerSocket();
            gState = GState.READY;
        } catch (Exception e)
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
        if (gState == GState.READY)
        {
            logger.info("server is running");
            gState = GState.RUNNING;
            while (gState == GState.RUNNING)
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
        } else
        {
            logger.fatal("server " + gState + " can not run");
        }
    }

    /**
     * Register protocol class.
     *
     * @param classNames the class names
     */
    public void registerProtocolClass(String... classNames)
    {
        for (String className : classNames)
        {
            try
            {
                serverInfos.getProtocolClasses().add(Class.forName(className));
                logger.info("register protocolClass: " + className);
            } catch (ClassNotFoundException e)
            {
                logger.error("unable to register protocolClass: " + className + " -> " + e.getMessage());
            }
        }
    }

    /**
     * Register protocol class.
     *
     * @param classes the classes
     */
    public void registerProtocolClass(Class<?>... classes)
    {
        for (Class<?> pclass : classes)
        {
            serverInfos.getProtocolClasses().add(pclass);
            logger.info("register protocolClass: " + pclass);
        }
    }

    /**
     * Gets server socket.
     *
     * @return the server socket
     */
    public ServerSocket getServerSocket()
    {
        return serverSocket;
    }

    /**
     * Gets protocol classes.
     *
     * @return the protocol classes
     */
    public ArrayList<Class<?>> getProtocolClasses()
    {
        return serverInfos.getProtocolClasses();
    }

    /**
     * Gets g state.
     *
     * @return the g state
     */
    public GState getGState()
    {
        return gState;
    }

    /**
     * Gets server infos.
     *
     * @return the server infos
     */
    public ServerInfos getServerInfos()
    {
        return serverInfos;
    }

    /**
     * Subscribe topic.
     *
     * @param topic   the topic
     * @param handler the handler
     */
    public synchronized void subscribeTopic(String topic, ProtocolHandler handler)
    {
        ArrayList<ProtocolHandler> handlers;
        if (!topicsMap.containsKey(topic))
        {
            handlers = new ArrayList<>();
            topicsMap.put(topic, handlers);
        } else
        {
            handlers = topicsMap.get(topic);
        }
        if (!handlers.contains(handler))
        {
            handlers.add(handler);
        }
    }

    /**
     * Unsubscribe topic.
     *
     * @param topic   the topic
     * @param handler the handler
     */
    public synchronized void unsubscribeTopic(String topic, ProtocolHandler handler)
    {
        if (topicsMap.containsKey(topic))
        {
            topicsMap.get(topic).remove(handler);
        }
    }

    private void unsubscribeTopic(ServerProtocolHandler serverProtocolHandler)
    {
        for (String key : topicsMap.keySet())
        {
            topicsMap.get(key).remove(serverProtocolHandler);
        }
    }

    /**
     * Topic notify subscribers.
     *
     * @param query                 the query
     * @param serverProtocolHandler the server protocol handler
     */
    public synchronized void topicNotifySubscribers(Query query, ServerProtocolHandler serverProtocolHandler)
    {
        String topic = query.getType();
        ArrayList<ProtocolHandler> handlers = topicsMap.get(topic);
        ArrayList<Serializable> newArgs = new ArrayList<>();
        newArgs.add(serverProtocolHandler.getIdentifier());
        newArgs.addAll(query.getArgs());
        query.setArgs(newArgs);
        for (ProtocolHandler handler : handlers)
        {
            handler.send(query);
        }
    }

    /**
     * Topic notify other subscribers.
     *
     * @param query                 the query
     * @param serverProtocolHandler the server protocol handler
     */
    public synchronized void topicNotifyOtherSubscribers(Query query, ServerProtocolHandler serverProtocolHandler)
    {
        String topic = query.getType();
        ArrayList<ProtocolHandler> handlers = topicsMap.get(topic);
        ArrayList<Serializable> newArgs = new ArrayList<>();
        newArgs.add(serverProtocolHandler.getIdentifier());
        newArgs.addAll(query.getArgs());
        query.setArgs(newArgs);
        for (ProtocolHandler handler : handlers)
        {
            if (handler != serverProtocolHandler)
                handler.send(query);
        }
    }

    /**
     * Notify other.
     *
     * @param query                 the query
     * @param serverProtocolHandler the server protocol handler
     */
    public synchronized void notifyOther(Query query, ServerProtocolHandler serverProtocolHandler)
    {
        ArrayList<Integer> ids = (ArrayList<Integer>) query.getArgs().get(0);
        query.getArgs().set(0, serverProtocolHandler.getIdentifier());
        for (ProtocolHandler handler : handlers)
        {
            if (ids.contains(handler.getIdentifier()))
            {
                handler.send(query);
            }
        }
    }

    /**
     * Remove.
     *
     * @param serverProtocolHandler the server protocol handler
     */
    public synchronized void remove(ServerProtocolHandler serverProtocolHandler)
    {
        handlers.remove(serverProtocolHandler);
        unsubscribeTopic(serverProtocolHandler);
    }

    /**
     * Subscribe queue.
     *
     * @param type                  the type
     * @param serverProtocolHandler the server protocol handler
     */
    public void subscribeQueue(String type, ServerProtocolHandler serverProtocolHandler)
    {
        Queue queue = queuesMap.getOrDefault(type, null);
        if (queue == null)
        {
            queue = new Queue();
            queuesMap.put(type, queue);
        }
        serverProtocolHandler.subscribeQueue(queue);
    }

    /**
     * Unsubscribe queue.
     *
     * @param type                  the type
     * @param serverProtocolHandler the server protocol handler
     */
    public void unsubscribeQueue(String type, ServerProtocolHandler serverProtocolHandler)
    {
        Queue queue = queuesMap.getOrDefault(type, null);
        if (queue != null)
        {
            serverProtocolHandler.unsubscribeQueue(queue);
        }
    }

    /**
     * Produce queue.
     *
     * @param query                 the query
     * @param serverProtocolHandler the server protocol handler
     */
    public void produceQueue(Query query, ServerProtocolHandler serverProtocolHandler)
    {
        Queue queue = queuesMap.getOrDefault(query.getType(), null);
        if (queue != null)
        {
            serverProtocolHandler.queueProduce(queue, query.setCategory(Query.Category.QUEUE_CONSUME));
        }
    }
}
