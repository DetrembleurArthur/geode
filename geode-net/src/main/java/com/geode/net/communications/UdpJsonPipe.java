package com.geode.net.communications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geode.net.connections.UdpConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class UdpJsonPipe extends UdpPipe<JSONObject>
{
    private final JSONParser parser;

    public UdpJsonPipe(UdpConnection<?> gateway, boolean serverMode)
    {
        super(gateway, serverMode);
        parser = new JSONParser();
        System.out.println("create UDP JSON pipe");
    }

    @Override
    public void send(JSONObject data) throws IOException
    {
        byte[] jsonBytes = data.toJSONString().getBytes(StandardCharsets.UTF_8);
        connection.sendBytes(jsonBytes);
        System.out.println("send JSON: " + data);
    }

    @Override
    public JSONObject recv() throws IOException, ParseException
    {
        System.out.println("wait JSON");
        byte[] bytes = connection.recvBytes(resendInfos);
        if(bytes != null)
        {
            resend();
            String jsonString = new String(bytes, StandardCharsets.UTF_8);
            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
            System.out.println("receive JSON: " + jsonObject);
            return jsonObject;
        }
        System.err.println("error at OBJECT reception");
        return new JSONObject();
    }

    public void send(Serializable data) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        byte[] jsonBytes = mapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8);
        connection.sendBytes(jsonBytes);
        System.out.println("send JSON: " + data);
    }

    public <T> T recv(Class<T> _class) throws Exception
    {
        System.out.println("wait JSON");
        byte[] bytes = connection.recvBytes(resendInfos);
        if(bytes != null)
        {
            resend();
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = new String(bytes, StandardCharsets.UTF_8);
            return (T) mapper.readValue(jsonString, _class);
        }
        System.err.println("error at OBJECT reception");
        return _class.getConstructor().newInstance();
    }
}
