package com.geode.test;

import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.Protocol;
import com.geode.net.ProtocolHandler;
import com.geode.net.Query;

@Protocol(value = "ping-pong")
public class Ping
{
    @Inject
    public ProtocolHandler handler;

    @Control
    public void pong()
    {
        System.out.println("pong");
        handler.send(Query.simple("yes"));
    }

    @Control(state = "OK")
    public void yes()
    {
        System.out.println("yes");
    }
}
