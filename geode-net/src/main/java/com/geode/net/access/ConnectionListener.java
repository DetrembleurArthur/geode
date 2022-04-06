package com.geode.net.access;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class ConnectionListener implements AutoCloseable
{
    private ServerSocket socket;

    public static ConnectionListener on(String host, int port, int backlog) throws IOException
    {
        ConnectionListener connectionListener = new ConnectionListener();
        ServerSocket socket = new ServerSocket(port, backlog, InetAddress.getByName(host));
        connectionListener.setSocket(socket);
        return connectionListener;
    }

    public static ConnectionListener local(int port, int backlog) throws IOException
    {
        return on("0.0.0.0", port, backlog);
    }

    public static ConnectionListener internal(int port, int backlog) throws IOException
    {
        return on("127.0.0.1", port, backlog);
    }

    private ConnectionListener()
    {

    }

    public Connection getConnection() throws IOException
    {
        return Connection.from(socket.accept());
    }

    public ServerSocket getSocket()
    {
        return socket;
    }

    public void setSocket(ServerSocket socket)
    {
        this.socket = socket;
    }

    @Override
    public void close() throws Exception
    {
        socket.close();
    }
}
