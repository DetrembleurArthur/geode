package com.geode.net.net.test;

import com.geode.net.net.annotations.Control;
import com.geode.net.net.annotations.Inject;
import com.geode.net.net.annotations.OnEvent;
import com.geode.net.net.annotations.Protocol;
import com.geode.net.net.annotations.Register;
import com.geode.net.net.Client;
import com.geode.net.net.Geode;
import com.geode.net.net.GeodeQuery;
import com.geode.net.net.ProtocolHandler;
import com.geode.net.net.channels.Channel;

@Protocol("p1")
public class ClientChannelProtocol1
{
    @Inject
    public ProtocolHandler handler;

    @Register
    public Geode geode;

    @Register("square")
    public Channel<Integer> square;

    public Channel<String> message;

    @OnEvent
    public void init()
    {
        handler.send(GeodeQuery.simple("hello"));
        message.set("Hello world!");
    }

    @Control
    public void soso()
    {
        System.err.println(geode);
        Client client = geode.launchClient("cli2");

        client.getHandlerSafe().send(GeodeQuery.simple("square").pack(6));
        
        float v = square.waitValue();

        client.getHandlerSafe().end();
        System.out.println("Value received: " + v);

    }
}
