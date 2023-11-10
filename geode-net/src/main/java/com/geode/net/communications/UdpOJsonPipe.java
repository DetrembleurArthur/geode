package com.geode.net.communications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geode.net.connections.UdpConnection;
import com.geode.net.query.Query;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class UdpOJsonPipe extends UdpPipe<Serializable>
{
    private final ObjectMapper mapper;
    private Class<?> _class = Query.class;

    public UdpOJsonPipe(UdpConnection<?> gateway, boolean serverMode)
    {
        super(gateway, serverMode);
        mapper = new ObjectMapper();
        System.out.println("create UDP OJSON pipe");
    }

    @Override
    public void send(Serializable data) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        byte[] jsonBytes = mapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8);
        connection.sendBytes(jsonBytes);
        System.out.println("send OJSON: " + data);
    }

    @Override
    public Serializable recv() throws Exception
    {
        System.out.println("wait OJSON");
        byte[] bytes = connection.recvBytes(resendInfos);
        if(bytes != null)
        {
            resend();
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = new String(bytes, StandardCharsets.UTF_8);
            return (Serializable) mapper.readValue(jsonString, _class);
        }
        System.err.println("error at OBJECT reception");
        return (Serializable) _class.getConstructor().newInstance();
    }

    public <T> UdpOJsonPipe prepareRecv(Class<T> _class)
    {
        this._class = _class;
        return this;
    }
}
