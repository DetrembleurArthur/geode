package com.geode.net.share;

import com.geode.net.access.Connection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonTunnel extends Tunnel<JSONObject, BufferedReader, BufferedWriter>
{
    private final JSONParser jsonParser;

    public JsonTunnel(Connection connection) throws IOException
    {
        super(connection);
        jsonParser = new JSONParser();
    }

    @Override
    protected void initStreams(InputStream inputStream, OutputStream outputStream)
    {
        setInputStream(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)));
        setOutputStream(new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)));
    }

    @Override
    public void send(JSONObject data) throws IOException
    {
        data.writeJSONString(outputStream);
        outputStream.newLine();
        outputStream.flush();
    }

    @Override
    public JSONObject recv() throws Exception
    {
        String buffer = inputStream.readLine();
        return (JSONObject) jsonParser.parse(buffer);
    }
}
