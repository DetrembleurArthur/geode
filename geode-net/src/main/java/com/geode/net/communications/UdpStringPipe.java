package com.geode.net.communications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geode.net.connections.UdpConnection;
import com.geode.net.query.Query;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class UdpStringPipe extends UdpPipe
{

    public UdpStringPipe(UdpConnection<?> gateway, boolean serverMode)
    {
        super(gateway, serverMode);
        System.out.println("create UDP String pipe");
    }

    @Override
    public void send(Serializable data) throws IOException
    {
        byte[] jsonBytes = data.toString().getBytes(StandardCharsets.UTF_8);
        super.send(jsonBytes);
        System.out.println("send String: " + data);
    }

    @Override
    public String recv() throws Exception
    {
        System.out.println("wait String");
        byte[] bytes = connection.recvBytes(resendInfos);
        String string = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("receive STRING: " + string);
        return string;
    }
}
