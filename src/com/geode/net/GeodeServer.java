package com.geode.net;

import com.geode.configurations.ServerConfigurations;
import com.geode.exceptions.GeodeException;
import com.geode.log.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GeodeServer implements Runnable, GeodeIdentifiable
{
    private final GeodeServerManager geodeServerManager;
    private final ServerConfigurations serverConfigurations;
    private Thread thread;
    private ServerSocket serverSocket;
    private boolean running = false;
    private final ArrayList<GeodeServerHandler> geodeHandlers;

    public GeodeServer(ServerConfigurations serverConfigurations, GeodeServerManager geodeServerManager) throws GeodeException
    {
        this.geodeServerManager = geodeServerManager;
        this.geodeHandlers = new ArrayList<>();
        this.serverConfigurations = serverConfigurations;
        init();
    }

    private void init() throws GeodeException
    {
        try
        {
            serverSocket = new ServerSocket(serverConfigurations.getPort(), 50, InetAddress.getByName(serverConfigurations.getIp()));
        } catch (IOException e)
        {
            throw new GeodeException(this, e.getMessage());
        }
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run()
    {
        running = true;
        while(running)
        {
            try
            {
                Log.out(this, "listen");
                Socket socket = serverSocket.accept();
                GeodeServerHandler geodeHandler = new GeodeServerHandler(socket, this);
                geodeHandlers.add(geodeHandler);
            } catch (IOException e)
            {
                Log.err(this, e.getMessage());
                running = false;
            } catch (GeodeException e)
            {
                Log.err(this, e.getMessage());
            }
        }
        Log.out(this, "is shutting down");
    }

    public ServerConfigurations getServerConfigurations()
    {
        return serverConfigurations;
    }

    public Thread getThread()
    {
        return thread;
    }

    public ServerSocket getServerSocket()
    {
        return serverSocket;
    }

    public boolean isRunning()
    {
        return running;
    }

    public ArrayList<GeodeServerHandler> getGeodeHandlers()
    {
        return geodeHandlers;
    }

    @Override
    public String getGeodeId()
    {
        return "GeodeServer::" + serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort();
    }
}
