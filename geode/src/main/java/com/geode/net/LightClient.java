package com.geode.net;

import com.geode.net.info.ClientInfos;
import com.geode.net.tunnels.TcpObjectTunnel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class LightClient extends AbstractClient
{
    private static final Logger logger = LogManager.getLogger(LightClient.class);
    private TcpObjectTunnel tcpObjectTunnel;

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
            tcpObjectTunnel = new TcpObjectTunnel(socket);
        } catch (Exception e)
        {
            logger.fatal("client connection error: " + e.getMessage(), getClientInfos().getName());
        }
    }

    public int send(Serializable serializable)
    {
        try
        {
            tcpObjectTunnel.send(serializable);
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
            return tcpObjectTunnel.recv();
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public TcpObjectTunnel getTcpTunnel()
    {
        return tcpObjectTunnel;
    }
}
