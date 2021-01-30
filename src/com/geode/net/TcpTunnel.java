package com.geode.net;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class TcpTunnel extends Tunnel<Socket>
{
    private static final Logger logger = Logger.getLogger(TcpTunnel.class);
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;

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

    public ObjectOutputStream getObjectOutputStream()
    {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream()
    {
        return objectInputStream;
    }
}
