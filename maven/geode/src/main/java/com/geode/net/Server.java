package com.geode.net;


import com.geode.logging.Logger;
import com.geode.net.tls.TLSUtils;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type Server.
 */
public class Server extends Thread implements Initializable
{
    private static final Logger logger = new Logger(Server.class);
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
            logger.info("enabling TLS...", getServerInfos().getName());
            SSLServerSocketFactory factory = TLSUtils.getServerSocketFactory(serverInfos);
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
        logger.info("initialisation", getServerInfos().getName());
        try
        {
            serverSocket = initServerSocket();
            gState = GState.READY;
        } catch (Exception e)
        {
            logger.fatal("unable to initialise the server: " + serverInfos + " -> " + e.getMessage(), getServerInfos().getName());
            gState = GState.BROKEN;
        }
        logger.info("init server: " + serverInfos, getServerInfos().getName());
    }

    @Override
    public void run()
    {
        init();
        if (gState == GState.READY)
        {
            logger.info("server is running", getServerInfos().getName());
            gState = GState.RUNNING;
            while (gState == GState.RUNNING)
            {
                try
                {
                    waitChargeAvailability();
                    Socket socket = serverSocket.accept();
                    logger.info("client connection accepted: " + socket, getServerInfos().getName());
                    ServerProtocolHandler handler = new ServerProtocolHandler(socket, this, serverInfos.isEnableDiscovery(),
                        serverInfos.getChannelsManager(), serverInfos.getChannelsManagerInfos());
                    handler.start();
                    handlers.add(handler);
                } catch (IOException e)
                {
                    logger.error("server accept error: " + e.getMessage(), getServerInfos().getName());
                }
                catch (InterruptedException e)
                {
                    logger.error("server charge error: " + e.getMessage(), getServerInfos().getName());
                }
            }
        } else
        {
            logger.fatal("server " + gState + " can not run", getServerInfos().getName());
        }
    }

    public synchronized void waitChargeAvailability() throws InterruptedException
    {
            while(handlers.size() >= serverInfos.getMaxHandlers())
            {
                logger.warning("wait for charge availability ", getServerInfos().getName());
                wait();
                logger.warning("free charge availability ", getServerInfos().getName());
            }
    }

    /**
     * Remove.
     *
     * @param serverProtocolHandler the server protocol handler
     */
    public synchronized void remove(ServerProtocolHandler serverProtocolHandler)
    {
        logger.warning("remove protocol handler", getServerInfos().getName());
        handlers.remove(serverProtocolHandler);
        unsubscribeTopic(serverProtocolHandler);
        if(handlers.size() + 1 >= serverInfos.getMaxHandlers())
            notify();
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
                logger.info("register protocolClass: " + className, getServerInfos().getName());
            } catch (ClassNotFoundException e)
            {
                logger.error("unable to register protocolClass: " + className + " -> " + e.getMessage(), getServerInfos().getName());
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
            logger.info("register protocolClass: " + pclass, getServerInfos().getName());
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
        logger.info("subscribe to topic : " + topic, getServerInfos().getName());
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
        logger.info("unsubscribe to topic : " + topic, getServerInfos().getName());
        if (topicsMap.containsKey(topic))
        {
            topicsMap.get(topic).remove(handler);
        }
    }

    private void unsubscribeTopic(ServerProtocolHandler serverProtocolHandler)
    {
        for (String key : topicsMap.keySet())
        {
            logger.info("unsubscribe to topic : " + key, getServerInfos().getName());
            topicsMap.get(key).remove(serverProtocolHandler);
        }
    }

    /**
     * Topic notify subscribers.
     *
     * @param geodeQuery                 the query
     * @param serverProtocolHandler the server protocol handler
     */
    public synchronized void topicNotifySubscribers(GeodeQuery geodeQuery, ServerProtocolHandler serverProtocolHandler)
    {
        String topic = geodeQuery.getType();
        ArrayList<ProtocolHandler> handlers = topicsMap.get(topic);
        ArrayList<Serializable> newArgs = new ArrayList<>();
        newArgs.add(serverProtocolHandler.getIdentifier());
        newArgs.addAll(geodeQuery.getArgs());
        geodeQuery.setArgs(newArgs);
        for (ProtocolHandler handler : handlers)
        {
            handler.send(geodeQuery);
        }
    }

    /**
     * Topic notify other subscribers.
     *
     * @param geodeQuery                 the query
     * @param serverProtocolHandler the server protocol handler
     */
    public synchronized void topicNotifyOtherSubscribers(GeodeQuery geodeQuery, ServerProtocolHandler serverProtocolHandler)
    {
        String topic = geodeQuery.getType();
        ArrayList<ProtocolHandler> handlers = topicsMap.get(topic);
        ArrayList<Serializable> newArgs = new ArrayList<>();
        newArgs.add(serverProtocolHandler.getIdentifier());
        newArgs.addAll(geodeQuery.getArgs());
        geodeQuery.setArgs(newArgs);
        for (ProtocolHandler handler : handlers)
        {
            if (handler != serverProtocolHandler)
                handler.send(geodeQuery);
        }
    }

    /**
     * Notify other.
     *
     * @param geodeQuery                 the query
     * @param serverProtocolHandler the server protocol handler
     */
    public synchronized void notifyOther(GeodeQuery geodeQuery, ServerProtocolHandler serverProtocolHandler)
    {
        ArrayList<Integer> ids = (ArrayList<Integer>) geodeQuery.getArgs().get(0);
        geodeQuery.getArgs().set(0, serverProtocolHandler.getIdentifier());
        for (ProtocolHandler handler : handlers)
        {
            if (ids.contains(handler.getIdentifier()))
            {
                handler.send(geodeQuery);
            }
        }
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
     * @param geodeQuery                 the query
     * @param serverProtocolHandler the server protocol handler
     */
    public void produceQueue(GeodeQuery geodeQuery, ServerProtocolHandler serverProtocolHandler)
    {
        Queue queue = queuesMap.getOrDefault(geodeQuery.getType(), null);
        if (queue != null)
        {
            serverProtocolHandler.queueProduce(queue, geodeQuery.setCategory(GeodeQuery.Category.QUEUE_CONSUME));
        }
    }

    public void end()
    {
        gState = GState.DOWN;
        try
        {
            getServerSocket().close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
