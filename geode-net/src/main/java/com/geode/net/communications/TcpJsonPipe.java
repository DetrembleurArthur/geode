package com.geode.net.communications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geode.net.connections.TcpConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TcpJsonPipe extends TcpPipe<JSONObject, BufferedReader, BufferedWriter>
{
    private final JSONParser parser;

    public TcpJsonPipe(TcpConnection connection) throws IOException
    {
        super(connection);
        parser = new JSONParser();
        System.out.println("create TCP JSON pipe");
    }

    @Override
    protected void initStreams(InputStream inputStream, OutputStream outputStream)
    {
        this.inputStream = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        this.outputStream = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        System.out.println("create TCP JSON streams");
    }

    @Override
    public void send(JSONObject data) throws IOException
    {
        data.writeJSONString(outputStream);
        outputStream.newLine();
        outputStream.flush();
        System.out.println("send JSON: " + data);
    }

    @Override
    public JSONObject recv() throws IOException, ParseException
    {
        System.out.println("wait JSON");
        String buffer = inputStream.readLine();
        System.out.println("receive JSON: " + buffer);
        return (JSONObject) parser.parse(buffer);
    }

    public void send(Serializable data) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(data);
        outputStream.write(value);
        outputStream.newLine();
        outputStream.flush();
    }

    public <T> T recv(Class<T> _class) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("wait JSON");
        String buffer = inputStream.readLine();
        System.out.println("receive JSON: " + buffer);
        return (T) mapper.readValue(buffer, _class);
    }
}
