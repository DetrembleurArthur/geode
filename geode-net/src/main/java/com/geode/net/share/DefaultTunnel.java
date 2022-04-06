package com.geode.net.share;

import com.geode.net.access.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DefaultTunnel extends Tunnel<byte[], InputStream, OutputStream>
{
    public DefaultTunnel(Connection connection) throws IOException
    {
        super(connection);
    }

    @Override
    protected void initStreams(InputStream inputStream, OutputStream outputStream) throws IOException
    {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void send(byte[] data) throws Exception
    {
        outputStream.write(data);
    }

    @Override
    public byte[] recv() throws Exception
    {
        return inputStream.readAllBytes();
    }
}
