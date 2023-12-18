package com.geode.net.communications;

import com.geode.net.connections.TcpConnection;

import java.io.*;
import java.util.Arrays;

public class TcpDataPipe extends TcpPipe<DataInputStream, DataOutputStream>
{
    public TcpDataPipe(TcpConnection connection) throws IOException
    {
        super(connection);
        System.out.println("create TCP DATA pipe");
    }

    @Override
    protected void initStreams(InputStream inputStream, OutputStream outputStream) throws IOException
    {
        this.outputStream = new DataOutputStream(outputStream);
        this.inputStream = new DataInputStream(inputStream);
        System.out.println("create TCP DATA streams");
    }

    @Override
    public void send(Serializable data) throws IOException
    {
        outputStream.write((byte[])data);
        outputStream.flush();
        System.out.println("send DATA: " + Arrays.toString((byte[]) data));
    }

    @Override
    public Serializable recv() throws IOException, ClassNotFoundException
    {
        System.out.println("wait DATA");
        byte[] data = inputStream.readAllBytes();
        System.out.println("receive DATA: " + Arrays.toString(data));
        return data;
    }
}
