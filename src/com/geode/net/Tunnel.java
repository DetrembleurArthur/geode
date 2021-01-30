package com.geode.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.Socket;

public abstract class Tunnel<T extends Closeable>
{
    protected final T socket;

    public Tunnel(T socket)
    {
        this.socket = socket;
    }

    public T getSocket()
    {
        return socket;
    }

    public abstract void send(Serializable serializable) throws IOException;

    public abstract  <T extends Serializable> T recv() throws IOException, ClassNotFoundException;

    public static Tunnel<?> build(Closeable socket) throws IOException
    {
        if(socket instanceof Socket)
        {
            return new TcpTunnel((Socket) socket);
        }
        else if(socket instanceof DatagramSocket)
        {
            return new UdpTunnel((DatagramSocket) socket);
            }
        return null;
    }
}
