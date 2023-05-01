package com.geode.net.communications;

import com.geode.net.connections.UdpConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
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
}
