package com.geode.net;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class Client implements Initializable, Runnable
{
    private static final Logger logger = Logger.getLogger(Client.class);
    protected final ClientInfos clientInfos;
    protected ClientProtocolHandler handler;
    protected GState gState;

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
			Socket socket = new Socket(getClientInfos().getHost(), getClientInfos().getPort());
			logger.info("client connected : " + socket);
			handler = new ClientProtocolHandler(socket, clientInfos.getProtocolClass());
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
            handler.start();
            logger.info("client handler is running");
        }
        else
        {
            logger.fatal("client " + gState + " can not run");
        }
    }

	public Class<?> getProtocolClass()
	{
		return clientInfos.getProtocolClass();
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
