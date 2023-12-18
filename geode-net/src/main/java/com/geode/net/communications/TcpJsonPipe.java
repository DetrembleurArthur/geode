package com.geode.net.communications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geode.net.connections.TcpConnection;
import com.geode.net.query.Query;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TcpJsonPipe extends TcpPipe<BufferedReader, BufferedWriter>
{
    private final ObjectMapper mapper;
    private Class<?> _class = Query.class;

    public TcpJsonPipe(TcpConnection connection) throws IOException
    {
        super(connection);
        mapper = new ObjectMapper();
        System.out.println("create TCP OJSON pipe");
    }

    @Override
    protected void initStreams(InputStream inputStream, OutputStream outputStream)
    {
        this.inputStream = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        this.outputStream = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        System.out.println("create TCP OJSON streams");
    }

    @Override
    public void send(Serializable data) throws IOException
    {
        String value = mapper.writeValueAsString(data);
        System.out.println("send OJSON: " + value);
        outputStream.write(value);
        outputStream.newLine();
        outputStream.flush();
    }

    @Override
    public Serializable recv() throws IOException
    {
        System.out.println("wait OJSON");
        String buffer = inputStream.readLine();
        System.out.println("receive OJSON: " + buffer);
        return (Serializable) mapper.readValue(buffer, _class);
    }

    public <T> TcpJsonPipe prepareRecv(Class<T> _class)
    {
        this._class = _class;
        return this;
    }
}
