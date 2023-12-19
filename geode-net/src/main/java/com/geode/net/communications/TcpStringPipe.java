package com.geode.net.communications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geode.net.connections.TcpConnection;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TcpStringPipe extends TcpPipe<BufferedReader, BufferedWriter>
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
    public void send(Serializable data) throws IOException
    {
        String value = data.toString();
        System.out.println("send String: " + value);
        outputStream.write(value);
        outputStream.newLine();
        outputStream.flush();
    }

    @Override
    public ArrayList<String> recv() throws IOException
    {
        System.out.println("wait String");
        String buffer;
        ArrayList<String> lines = new ArrayList<>();
        while(!(buffer = inputStream.readLine()).isBlank())
        {
            lines.add(buffer);
            System.out.println("receive line: " + buffer);
        }
        return lines;
    }
}
