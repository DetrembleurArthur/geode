package com.geode.net.mgmt;

import com.geode.net.access.Connection;
import com.geode.net.share.DefaultTunnel;
import com.geode.net.share.JsonTunnel;
import com.geode.net.share.ObjectTunnel;
import com.geode.net.share.Tunnel;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionHandler implements Runnable, AutoCloseable
{
    public static class State
    {
        public static int STOPPED = 0;
        public static int INIT = 1;
        public static int RUNNING = 2;
    }

    private final Connection connection;
    private final Server server;
    private final Thread thread;
    private Tunnel<?, ?, ?> tunnel;
    private Byte tunnelMode;
    private final AtomicInteger state = new AtomicInteger(State.STOPPED);

    public ConnectionHandler(Connection connection, Server server) throws IOException
    {
        this.connection = connection;
        this.server = server;
        tunnel = new DefaultTunnel(connection);
        thread = new Thread(this);
        tunnelMode = Handshake.OBJECT_MODE;
        state.set(State.INIT);
    }

    public ConnectionHandler(Connection connection) throws IOException
    {
        this(connection, null);
    }

    public void runAsThread()
    {
        thread.start();
    }

    @Override
    public void run()
    {
        try
        {
            handshake();
            state.set(State.RUNNING);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private synchronized void handshake() throws Exception
    {
        System.out.println("Handshake");
        if(server == null)
        {
            ((DefaultTunnel)tunnel).send(new byte[]{
                    tunnelMode
            });
            ((DefaultTunnel)tunnel).setSize(1);
            byte[] data = ((DefaultTunnel)tunnel).recv();
            if(data.length >= 1)
            {
                if(data[0] == Handshake.OK)
                {
                    tunnel = Tunnel.create(tunnelMode, connection);
                }
            }
        }
        else
        {
            ((DefaultTunnel)tunnel).setSize(1);
            byte[] data = ((DefaultTunnel)tunnel).recv();
            if(data.length >= 1)
            {
                if(data[0] >= Handshake.OBJECT_MODE && data[0] <= Handshake.JSON_MODE)
                {
                    tunnelMode = data[0];
                    ((DefaultTunnel)tunnel).send(new byte[]{
                            Handshake.OK
                    });
                    tunnel = Tunnel.create(tunnelMode, connection);
                }
            }
        }
        notify();
    }

    public synchronized Tunnel<?, ?, ?> getTunnel()
    {
        while(state.get() != State.RUNNING)
        {
            try
            {
                wait(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return tunnel;
    }

    public JsonTunnel json()
    {
        return (JsonTunnel) getTunnel();
    }

    public ObjectTunnel obj()
    {
        return (ObjectTunnel) getTunnel();
    }

    @Override
    public void close()
    {
        try
        {
            connection.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        server.removeConnectionHandler(this);
    }

    public Byte getTunnelMode()
    {
        return tunnelMode;
    }

    public void setTunnelMode(Byte tunnelMode)
    {
        this.tunnelMode = tunnelMode;
    }
}
