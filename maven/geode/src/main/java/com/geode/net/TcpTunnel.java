package com.geode.net;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * The type Tcp tunnel.
 */
public class TcpTunnel extends Tunnel<Socket>
{
    private static final Logger logger = LogManager.getLogger(TcpTunnel.class);
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;

    /**
     * Instantiates a new Tcp tunnel.
     *
     * @param socket the socket
     * @throws IOException the io exception
     */
    public TcpTunnel(Socket socket) throws IOException
    {
        super(socket);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        logger.info("tunnel initialize on " + socket);
    }

    @Override
    public void send(Serializable serializable) throws IOException
    {
        logger.info("send " + serializable);
        objectOutputStream.writeObject(serializable);
        objectOutputStream.flush();
    }

    @Override
    public <T extends Serializable> T recv() throws IOException, ClassNotFoundException
    {
        T obj = (T) objectInputStream.readObject();
        logger.info("recv " + obj);
        return obj;
    }

    /**
     * Gets object output stream.
     *
     * @return the object output stream
     */
    public ObjectOutputStream getObjectOutputStream()
    {
        return objectOutputStream;
    }

    /**
     * Gets object input stream.
     *
     * @return the object input stream
     */
    public ObjectInputStream getObjectInputStream()
    {
        return objectInputStream;
    }
}
