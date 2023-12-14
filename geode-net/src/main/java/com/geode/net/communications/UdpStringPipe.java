package com.geode.net.communications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geode.net.connections.UdpConnection;
import com.geode.net.query.Query;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class UdpStringPipe extends UdpPipe<String>
{

    public UdpStringPipe(UdpConnection<?> gateway, boolean serverMode)
    {
        super(gateway, serverMode);
        System.out.println("create UDP String pipe");
    }

    @Override
    public void send(String data) throws IOException
    {
        byte[] jsonBytes = data.getBytes(StandardCharsets.UTF_8);
        connection.sendBytes(jsonBytes);
        System.out.println("send String: " + data);
    }

    @Override
    public String recv() throws Exception
    {
        System.out.println("wait String");
        byte[] bytes = connection.recvBytes(resendInfos);
        if(bytes != null)
        {
            resend();
            String s = new String(bytes, StandardCharsets.UTF_8);
            return s;
        }
        System.err.println("error at String reception");
        return "";
    }
}
