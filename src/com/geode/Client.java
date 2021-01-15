package com.geode;


import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnInit;
import com.geode.annotations.Protocol;
import com.geode.net.GeodeHandler;
import com.geode.net.Query;

@Protocol
public class Client
{
    @Inject
    public GeodeHandler geodeHandler;

    @OnInit
    public void start_ping()
    {
        geodeHandler.send(Query.simple("ping"));
        System.out.println("on init");
    }

    @Control
    public void pong(String msg)
    {
        System.out.println("pong recieved : " + msg);
    }
}
