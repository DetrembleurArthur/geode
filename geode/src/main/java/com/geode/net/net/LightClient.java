package com.geode.net.net;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import com.geode.net.net.logging.Logger;

public class LightClient extends AbstractClient
{
    private static final Logger logger = new Logger(LightClient.class);
    private TcpTunnel tcpTunnel;

    public LightClient(ClientInfos infos)
    {
        super(infos);
    }

    @Override
    public void init()
    {
        logger.info("initialisation", getClientInfos().getName());
        try
        {
            Socket socket = initSocket();
            logger.info("client connected : " + socket, getClientInfos().getName());
            tcpTunnel = new TcpTunnel(socket);
        } catch (Exception e)
        {
            logger.fatal("client connection error: " + e.getMessage(), getClientInfos().getName());
        }
    }

    public int send(Serializable serializable)
    {
        try
        {
            tcpTunnel.send(serializable);
        } catch (IOException e)
        {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public <T> T recv()
    {
        try
        {
            return tcpTunnel.recv();
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public TcpTunnel getTcpTunnel()
    {
        return tcpTunnel;
    }
}
