package com.geode.net.communications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geode.net.connections.TcpConnection;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TcpStringPipe extends TcpPipe<String, BufferedReader, BufferedWriter>
{

    public TcpStringPipe(TcpConnection connection) throws IOException
    {
        super(connection);
        System.out.println("create TCP String pipe");
    }

    @Override
    protected void initStreams(InputStream inputStream, OutputStream outputStream)
    {
        this.inputStream = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        this.outputStream = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        System.out.println("create TCP String streams");
    }

    @Override
    public void send(String data) throws IOException
    {
        String value = data;
        System.out.println("send String: " + value);
        outputStream.write(value);
        outputStream.newLine();
        outputStream.flush();
    }

    @Override
    public String recv() throws IOException
    {
        System.out.println("wait String");
        String buffer = inputStream.readLine();
        System.out.println("receive String: " + buffer);
        return buffer;
    }
}
