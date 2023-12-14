package com.geode.net.connections;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import com.geode.crypto.Store;
import com.geode.crypto.Tls;

public class TcpStickyConnection implements Closeable, Connection
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

    public static TcpStickyConnection onSecure(String host, int port, int backlog, Store kStore) throws Exception
    {
        ServerSocket socket = Tls.getServerSocketFactoryKeystore(kStore).createServerSocket(port, backlog, InetAddress.getByName(host));
        return new TcpStickyConnection(socket);
    }

    public static TcpStickyConnection onSecure(String host, int port, int backlog, String ca, String cert, String key) throws Exception
    {
        ServerSocket socket = Tls.getServerSocketFactory(ca, cert, key).createServerSocket(port, backlog, InetAddress.getByName(host));
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

    public static TcpStickyConnection localSecure(int port, int backlog, Store kStore) throws Exception
    {
        return onSecure("0.0.0.0", port, backlog, kStore);
    }

    public static TcpStickyConnection internalSecure(int port, int backlog, Store kStore) throws Exception
    {
        return onSecure("127.0.0.1", port, backlog, kStore);
    }

    public static TcpStickyConnection localSecure(int port, int backlog, String ca, String cert, String key) throws Exception
    {
        return onSecure("0.0.0.0", port, backlog, ca, cert, key);
    }

    public static TcpStickyConnection internalSecure(int port, int backlog, String ca, String cert, String key) throws Exception
    {
        return onSecure("127.0.0.1", port, backlog, ca, cert, key);
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
