package com.geode.net.mgmt;

import com.geode.net.access.Connection;
import com.geode.net.access.ConnectionListener;

import java.io.IOException;
import java.util.HashMap;

public class Server implements Runnable, AutoCloseable
{
    public static class State
    {
        public static final int STOPPED = 0;
        public static final int READY = 1;
        public static final int RUNNING = 2;
        public static final int BROKEN = -1;
    }

    public static class Error
    {
        public static final int LISTEN_CONNECTION_ERROR = 0;
    }

    private final ConnectionListener connectionListener;
    private final HashMap<Integer, ErrorHandler> errorHandlerHashMap = new HashMap<>();
    private final ConnectionHandler connectionHandler;
    private int state;

    public Server(ConnectionListener connectionListener, ConnectionHandler connectionHandler)
    {
        this.connectionHandler = connectionHandler;
        this.state = State.STOPPED;
        this.connectionListener = connectionListener;
    }

    protected void init()
    {
        this.state = State.READY;
    }

    @Override
    public void run()
    {
        init();
        if(this.state == State.READY)
        {
            this.state = State.RUNNING;
            while(this.state == State.RUNNING)
            {
                try
                {
                    Connection connection = connectionListener.getConnection();
                    connectionHandler.handleConnection(connection);
                } catch (IOException e)
                {
                    this.state = State.BROKEN;
                    callErrorHandler(Error.LISTEN_CONNECTION_ERROR, e);
                }
            }
        }
    }

    @Override
    public void close() throws Exception
    {
        connectionHandler.close();
        connectionListener.close();
    }

    protected void callErrorHandler(int errcode, Throwable e)
    {
        e.printStackTrace();
        ErrorHandler handler = errorHandlerHashMap.get(errcode);
        if(handler != null)
            handler.handle(e);
    }

    public void onError(int errcode, ErrorHandler handler)
    {
        errorHandlerHashMap.put(errcode, handler);
    }

    public int getState()
    {
        return state;
    }

    public ConnectionListener getConnectionListener()
    {
        return connectionListener;
    }
}
