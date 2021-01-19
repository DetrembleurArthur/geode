package com.geode.net;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Tunnel
{
    private static final Logger logger = Logger.getLogger(Tunnel.class);
    private final Socket socket;
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;

    public Tunnel(Socket socket) throws IOException
    {
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.socket = socket;
        logger.info("tunnel initialize on " + socket);
    }

    public void send(Serializable serializable) throws IOException
    {
        logger.info("send " + serializable);
        objectOutputStream.writeObject(serializable);
        objectOutputStream.flush();
    }

    public <T extends Serializable> T recv() throws IOException, ClassNotFoundException
    {
        T obj = (T) objectInputStream.readObject();
        logger.info("recv " + obj);
        return obj;
    }

    public Socket getSocket()
    {
        return socket;
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
