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
    private ClientProtocolHandler handler;
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
    	init();
        if(gState == GState.READY)
        {
            gState = GState.RUNNING;
            handler = new ClientProtocolHandler(socket, protocolClass);
            handler.start();
            logger.info("client handler is running");
        }
        else
        {
            logger.fatal("client " + gState + " can not run");
        }
    }

	public Socket getSocket()
	{
		return socket;
	}

	public void setSocket(Socket socket)
	{
		this.socket = socket;
	}

	public Class<?> getProtocolClass()
	{
		return protocolClass;
	}

	public void setProtocolClass(Class<?> protocolClass)
	{
		this.protocolClass = protocolClass;
	}

	public ClientProtocolHandler getHandler()
	{
		return handler;
	}
	
	public ClientProtocolHandler getHandlerSafe()
	{
		while(!handler.isRunning());
		return handler;
	}

	public void setHandler(ClientProtocolHandler handler)
	{
		this.handler = handler;
	}

	public GState getgState()
	{
		return gState;
	}

	public void setgState(GState gState)
	{
		this.gState = gState;
	}

	public ClientInfos getClientInfos()
	{
		return clientInfos;
	}
    
    
}
