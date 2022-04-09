package com.geode.net.mgmt;

import com.geode.net.access.Connection;
import com.geode.net.access.ConnectionListener;

import java.io.IOException;
import java.util.ArrayList;
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
    private int state;
    private Thread thread;
    private final ArrayList<ConnectionHandler> connectionHandlers = new ArrayList<>();

    public Server(ConnectionListener connectionListener)
    {
        this.state = State.STOPPED;
        this.connectionListener = connectionListener;
    }

    protected synchronized void init()
    {
        setState(State.READY);
    }

    public Thread runAsThread()
    {
        thread = new Thread(this);
        thread.start();
        return thread;
    }

    @Override
    public void run()
    {
        init();
        if(getState() == State.READY)
        {
            setState(State.RUNNING);
            while(getState() == State.RUNNING)
            {
                try
                {
                    Connection connection = connectionListener.getConnection();
                    handleConnection(connection);
                } catch (IOException e)
                {
                    setState(State.BROKEN);
                    callErrorHandler(Error.LISTEN_CONNECTION_ERROR, e);
                }
            }
        }
    }

    private synchronized void handleConnection(Connection connection) throws IOException
    {
        ConnectionHandler handler = new ConnectionHandler(connection, this);
        connectionHandlers.add(handler);
        handler.runAsThread();
    }

    @Override
    public synchronized void close() throws Exception
    {
        connectionHandlers.forEach(ConnectionHandler::close);
        connectionListener.close();
    }

    public synchronized void removeConnectionHandler(ConnectionHandler handler)
    {
        System.out.println("Remove handler");
        connectionHandlers.remove(handler);
    }

    protected synchronized void callErrorHandler(int errcode, Throwable e)
    {
        e.printStackTrace();
        ErrorHandler handler = errorHandlerHashMap.get(errcode);
        if(handler != null)
            handler.handle(e);
    }

    public synchronized void onError(int errcode, ErrorHandler handler)
    {
        errorHandlerHashMap.put(errcode, handler);
    }

    public synchronized int getState()
    {
        return state;
    }

    public synchronized void setState(int state)
    {
        this.state = state;
    }

    public synchronized ConnectionListener getConnectionListener()
    {
        return connectionListener;
    }

    public Thread getThread()
    {
        return thread;
    }
}
