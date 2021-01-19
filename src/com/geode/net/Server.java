package com.geode.net;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server extends Thread implements Initializable
{
    private static Logger logger = Logger.getLogger(Server.class);
    private ServerSocket serverSocket;
    private final ArrayList<Class<?>> protocolClasses;
    private final CopyOnWriteArrayList<ProtocolHandler> handlers;
    private GState gState;
    private final ServerInfos serverInfos;

    public Server(String host, int backlog, int port)
    {
        this(new ServerInfos(host, backlog, port));
    }

    public Server(ServerInfos serverInfos)
    {
        this.serverInfos = serverInfos;
        gState = GState.DOWN;
        protocolClasses = new ArrayList<>();
        handlers = new CopyOnWriteArrayList<>();
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
                    ServerProtocolHandler handler = new ServerProtocolHandler(socket, protocolClasses);
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
                protocolClasses.add(Class.forName(className));
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
            protocolClasses.add(pclass);
            logger.info("register protocolClass: " + pclass);
        }
    }

    public ServerSocket getServerSocket()
    {
        return serverSocket;
    }

    public ArrayList<Class<?>> getProtocolClasses()
    {
        return protocolClasses;
    }

    public GState getGState()
    {
        return gState;
    }

    public ServerInfos getServerInfos()
    {
        return serverInfos;
    }
}
