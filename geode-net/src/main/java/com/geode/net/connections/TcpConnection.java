package com.geode.net.connections;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public class TcpConnection implements Closeable
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

    public static TcpConnection local(int port) throws IOException
    {
        return on("0.0.0.0", port);
    }

    public static TcpConnection internal(int port) throws IOException
    {
        return on("127.0.0.1", port);
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
