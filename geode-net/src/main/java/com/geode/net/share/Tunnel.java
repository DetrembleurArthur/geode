package com.geode.net.share;

import com.geode.net.access.Connection;
import com.geode.net.mgmt.Handshake;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

public abstract class Tunnel<T, I, O>
{
    protected I inputStream;
    protected O outputStream;

    public static Tunnel<?, ?, ?> create(Byte mode, Connection connection) throws IOException
    {
        if(Objects.equals(mode, Handshake.JSON_MODE))
            return new JsonTunnel(connection);
        else if(Objects.equals(mode, Handshake.OBJECT_MODE))
            return new ObjectTunnel(connection);
        return null;
    }

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
