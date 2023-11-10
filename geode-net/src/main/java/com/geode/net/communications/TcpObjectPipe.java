package com.geode.net.communications;

import com.geode.net.connections.TcpConnection;

import java.io.*;

public class TcpObjectPipe extends TcpPipe<Serializable, ObjectInputStream, ObjectOutputStream>
{
    public TcpObjectPipe(TcpConnection connection) throws IOException
    {
        super(connection);
        System.out.println("create TCP OBJECT pipe");
    }

    @Override
    protected void initStreams(InputStream inputStream, OutputStream outputStream) throws IOException
    {
        this.outputStream = new ObjectOutputStream(outputStream);
        this.inputStream = new ObjectInputStream(inputStream);
        System.out.println("create TCP OBJECT streams");
    }

    @Override
    public void send(Serializable data) throws IOException
    {
        outputStream.writeObject(data);
        outputStream.flush();
        System.out.println("send OBJECT: " + data);
    }

    @Override
    public Serializable recv() throws IOException, ClassNotFoundException
    {
        System.out.println("wait OBJECT");
        Serializable serializable = (Serializable) inputStream.readObject();
        System.out.println("receive OBJECT: " + serializable);
        return serializable;
    }
}
