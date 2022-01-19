package com.geode.net.net.test;

import com.geode.net.net.annotations.Control;
import com.geode.net.net.annotations.Inject;
import com.geode.net.net.annotations.OnEvent;
import com.geode.net.net.annotations.Protocol;
import com.geode.net.net.GeodeQuery;
import com.geode.net.net.ProtocolHandler;

@Protocol("test")
public class ClientProtocol
{
    @Inject
    public ProtocolHandler handler;

    @OnEvent
    public void init()
    {
        handler.send(GeodeQuery.simple("ping").pack("Hello world!"));
    }

    @Control
    public void pong(String message)
    {
        System.out.println("Receive from server: " + message);
    }
}
