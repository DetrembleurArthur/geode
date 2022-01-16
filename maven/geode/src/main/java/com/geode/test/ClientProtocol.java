package com.geode.test;

import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnEvent;
import com.geode.annotations.Protocol;
import com.geode.annotations.Register;
import com.geode.net.Geode;
import com.geode.net.GeodeQuery;
import com.geode.net.ProtocolHandler;

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
