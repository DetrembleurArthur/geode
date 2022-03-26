package com.geode.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Connection implements AutoCloseable
{
    private Socket socket;

    public static Connection to(String host, int port, String local, int localPort) throws IOException
    {
        Connection connection = new Connection();
        Socket socket;
        if(localPort > 0)
        {
            socket = new Socket(host, port, InetAddress.getByName(local), localPort);
        }
        else
        {
            socket = new Socket(host, port);
        }
        connection.setSocket(socket);
        return connection;
    }

    public static Connection to(String host, int port) throws IOException
    {
        return to(host, port, "", -1);
    }

    public static Connection local(int port) throws IOException
    {
        return to("0.0.0.0", port);
    }

    public static Connection internal(int port) throws IOException
    {
        return to("127.0.0.1", port);
    }

    private Connection()
    {

    }

    public static Connection from(Socket socket)
    {
        Connection connection = new Connection();
        connection.setSocket(socket);
        return connection;
    }

    public Socket getSocket()
    {
        return socket;
    }

    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void close() throws Exception
    {
        socket.close();
    }
}
