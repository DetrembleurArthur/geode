package com.geode.test;

import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.Protocol;
import com.geode.net.ProtocolHandler;

@Protocol("ping-pong")
public class Pong
{
    @Inject
    public ProtocolHandler handler;

    @Control
    public String ping()
    {
        System.out.println("ping");
        handler.setProtocolState("OK");
        return "pong";
    }

    @Control(state = "OK")
    public String yes()
    {
        return "yes";
    }
}
