package com.geode.net;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class Client implements Initializable, Runnable
{
    private static final Logger logger = Logger.getLogger(Client.class);
    private Socket socket;
    private Class<?> protocolClass;
    private final ClientInfos clientInfos;
    private GState gState;

    public Client(String host, int port)
    {
        this(new ClientInfos(host, port));
    }

    public Client(ClientInfos clientInfos)
    {
        this.clientInfos = clientInfos;
        gState = GState.DOWN;
    }

    @Override
    public void init()
    {
        try
        {
            socket = new Socket(clientInfos.getHost(), clientInfos.getPort());
            logger.info("client connected : " + socket);
            gState = GState.READY;
        } catch (IOException e)
        {
            logger.fatal("client connection error: " + e.getMessage());
            gState = GState.BROKEN;
        }
    }

    @Override
    public void run()
    {
        if(gState == GState.READY)
        {
            gState = GState.RUNNING;

        }
        else
        {
            logger.fatal("client " + gState + " can not run");
        }
    }
}
