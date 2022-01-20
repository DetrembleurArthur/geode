package com.geode.net.tunnels;


import com.geode.crypto.pipeline.Pipeline;
import com.geode.net.queries.GeodeQuery;
import com.geode.net.queries.LowQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The type Tcp tunnel.
 */
public class TcpObjectTunnel extends Tunnel<Socket>
{
    private static final Logger logger = LogManager.getLogger(TcpObjectTunnel.class);
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;
    private final Pipeline pipeline = new Pipeline();

    /**
     * Instantiates a new Tcp tunnel.
     *
     * @param socket the socket
     * @throws IOException the io exception
     */
    public TcpObjectTunnel(Socket socket) throws IOException
    {
        super(socket);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        logger.info("tunnel initialized on " + socket);
    }

    @Override
    public void send(Serializable serializable) throws IOException
    {
        logger.info("send " + serializable);
        objectOutputStream.writeObject(pipeline.out(serializable));
        objectOutputStream.flush();
    }

    @Override
    public <T extends Serializable> T recv() throws IOException, ClassNotFoundException
    {
        T obj = (T) pipeline.in((Serializable) objectInputStream.readObject());
        logger.info("recv " + obj);
        return obj;
    }

    public GeodeQuery recvQuery() throws IOException, ClassNotFoundException
    {
        return recv();
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
