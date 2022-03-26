package com.geode.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class Tunnel<T, I, O>
{
    protected I inputStream;
    protected O outputStream;

    public Tunnel(final Connection connection) throws IOException
    {
        Socket socket = connection.getSocket();
        initStreams(socket.getInputStream(), socket.getOutputStream());
    }

    protected void setInputStream(I inputStream)
    {
        this.inputStream = inputStream;
    }

    protected void setOutputStream(O outputStream)
    {
        this.outputStream = outputStream;
    }

    public I getInputStream()
    {
        return inputStream;
    }

    public O getOutputStream()
    {
        return outputStream;
    }

    protected abstract void initStreams(InputStream inputStream, OutputStream outputStream) throws IOException;

    public abstract void send(T data) throws Exception;
    public abstract T recv() throws Exception;
}
