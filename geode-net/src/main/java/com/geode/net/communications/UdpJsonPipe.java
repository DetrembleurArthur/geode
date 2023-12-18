package com.geode.net.communications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geode.crypto.Serializer;
import com.geode.net.connections.UdpConnection;
import com.geode.net.query.Query;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class UdpJsonPipe extends UdpPipe
{
    private Class<?> _class = Query.class;

    public UdpJsonPipe(UdpConnection<?> gateway, boolean serverMode)
    {
        super(gateway, serverMode);
        System.out.println("create UDP JSON pipe");
    }

    @Override
    public void send(Serializable data) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        byte[] jsonBytes = mapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8);
        super.send(jsonBytes);
        System.out.println("send JSON: " + data);
    }

    @Override
    public Serializable recv() throws Exception
    {
        System.out.println("wait JSON");
        byte[] bytes = (byte[]) super.recv();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("receive JSON: " + jsonString);
        return (Serializable) mapper.readValue(jsonString, _class);
    }

    public <T> UdpJsonPipe prepareRecv(Class<T> _class)
    {
        this._class = _class;
        return this;
    }
}
