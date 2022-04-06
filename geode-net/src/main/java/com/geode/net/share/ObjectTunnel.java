package com.geode.net.share;

import com.geode.net.access.Connection;

import java.io.*;

public class ObjectTunnel extends Tunnel<Serializable, ObjectInputStream, ObjectOutputStream>
{
    public ObjectTunnel(Connection connection) throws IOException
    {
        super(connection);
    }

    @Override
    protected void initStreams(InputStream inputStream, OutputStream outputStream) throws IOException
    {
        setOutputStream(new ObjectOutputStream(outputStream));
        setInputStream(new ObjectInputStream(inputStream));
    }

    @Override
    public void send(Serializable data) throws IOException
    {
        outputStream.writeObject(data);
        outputStream.flush();
    }

    @Override
    public Serializable recv() throws IOException
    {
        try
        {
            return (Serializable) inputStream.readObject();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
