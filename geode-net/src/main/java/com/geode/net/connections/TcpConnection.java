package com.geode.net.connections;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

import com.geode.crypto.Store;
import com.geode.crypto.Tls;

import javax.net.ssl.SSLSocket;

public class TcpConnection implements Closeable, Connection
{
    private final Socket socket;

    TcpConnection(Socket socket)
    {
        this.socket = socket;
        System.out.println("create TCP connection: " + socket);
    }

    public static TcpConnection on(String ip, int port) throws IOException
    {
        Socket socket = new Socket(ip, port);
        return new TcpConnection(socket);
    }

    public static TcpConnection onSecure(String ip, int port, Store kStore) throws Exception
    {
        Socket socket = Tls.getSocketFactoryKeystore(kStore).createSocket(ip, port);
        return new TcpConnection(socket);
    }

    public static TcpConnection onSecure(String ip, int port, String ca, String cert, String key) throws Exception
    {
        Socket socket = Tls.getSocketFactory(ca, cert, key).createSocket(ip, port);
        return new TcpConnection(socket);
    }

    public static TcpConnection local(int port) throws IOException
    {
        return on("0.0.0.0", port);
    }

    public static TcpConnection internal(int port) throws IOException
    {
        return on("127.0.0.1", port);
    }

    public static TcpConnection localSecure(int port, Store kStore) throws Exception
    {
        return onSecure("0.0.0.0", port, kStore);
    }

    public static TcpConnection internalSecure(int port, Store kStore) throws Exception
    {
        return onSecure("127.0.0.1", port, kStore);
    }

    public static TcpConnection localSecure(int port, String ca, String cert, String key) throws Exception
    {
        return onSecure("0.0.0.0", port, ca, cert, key);
    }

    public static TcpConnection internalSecure(int port, String ca, String cert, String key) throws Exception
    {
        return onSecure("127.0.0.1", port, ca, cert, key);
    }

    @Override
    public void close() throws IOException
    {
        socket.close();
        System.out.println("close TCP connection: " + socket);
    }

    public Socket getSocket()
    {
        return socket;
    }
}
