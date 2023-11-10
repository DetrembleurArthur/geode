package com.geode.net.communications;

import com.geode.net.connections.TcpConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class TcpPipe<T, I, O> implements Pipe<T>
{
    private final TcpConnection connection;
    protected I inputStream;
    protected O outputStream;

    public TcpPipe(TcpConnection connection) throws IOException
    {
        this.connection = connection;
        System.out.println("create TCP pipe");
        initStreams(connection.getSocket().getInputStream(), connection.getSocket().getOutputStream());
    }

    protected abstract void initStreams(InputStream inputStream, OutputStream outputStream) throws IOException;

    @Override
    public void close() throws IOException
    {
        connection.close();
        System.out.println("close TCP pipe");
    }

    @Override
    public boolean available() {
        return connection.getSocket().isConnected() || !connection.getSocket().isClosed();
    }
}
