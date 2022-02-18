package com.geode.net.tunnels;

import com.geode.net.info.CommunicationModes;
import com.geode.net.queries.GeodeQuery;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * The type Tunnel.
 *
 * @param <T> the type parameter
 */
public abstract class Tunnel<T extends Closeable>
{
    /**
     * The Socket.
     */
    protected final T socket;

    /**
     * Instantiates a new Tunnel.
     *
     * @param socket the socket
     */
    public Tunnel(T socket)
    {
        this.socket = socket;
    }

    /**
     * Build tunnel.
     *
     * @param socket the socket
     * @param mode
     * @return the tunnel
     * @throws IOException the io exception
     */
    public static Tunnel<?> build(Closeable socket, CommunicationModes mode) throws IOException
    {
        if (socket instanceof Socket)
        {
            switch (mode)
            {
                case BYTES:
                    return new TcpBytesTunnel((Socket) socket);
                case OBJECT:
                    return new TcpObjectTunnel((Socket) socket);
                case JSON:
                    return new TcpJsonTunnel((Socket) socket);
            }

        } else if (socket instanceof DatagramSocket)
        {
            return new UdpTunnel((DatagramSocket) socket);
        }
        return null;
    }

    /**
     * Gets socket.
     *
     * @return the socket
     */
    public T getSocket()
    {
        return socket;
    }

    /**
     * Send.
     *
     * @param serializable the serializable
     * @throws IOException the io exception
     */
    public abstract void send(Serializable serializable) throws IOException;

    public void sendQuery(GeodeQuery query) throws IOException
    {
        send(query);
    }

    /**
     * Recv t.
     *
     * @param <T> the type parameter
     * @return the t
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public abstract <T extends Serializable> T recv() throws IOException, ClassNotFoundException;

    public GeodeQuery recvQuery() throws IOException, ClassNotFoundException
    {
        return recv();
    }
}
