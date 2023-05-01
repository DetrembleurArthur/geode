package com.geode.net.connections;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class TcpStickyConnection implements Closeable
{
    private final ServerSocket socket;

    private TcpStickyConnection(ServerSocket socket)
    {
        this.socket = socket;
        System.out.println("create sticky TCP connection: " + socket);
    }

    public static TcpStickyConnection on(String host, int port, int backlog) throws IOException
    {
        ServerSocket socket = new ServerSocket(port, backlog, InetAddress.getByName(host));
        return new TcpStickyConnection(socket);
    }

    public static TcpStickyConnection local(int port, int backlog) throws IOException
    {
        return on("0.0.0.0", port, backlog);
    }

    public static TcpStickyConnection internal(int port, int backlog) throws IOException
    {
        return on("127.0.0.1", port, backlog);
    }

    public TcpConnection accept() throws IOException
    {
        System.out.println("accept TCP entrance connection from " + socket);
        return new TcpConnection(socket.accept());
    }

    @Override
    public void close() throws IOException
    {
        socket.close();
        System.out.println("close sticky TCP connection: " + socket);
    }

    public ServerSocket getSocket()
    {
        return socket;
    }
}
