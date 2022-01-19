package com.geode.net.net.test;

import com.geode.net.net.annotations.Control;
import com.geode.net.net.annotations.OnEvent;
import com.geode.net.net.annotations.Protocol;
import com.geode.net.net.GeodeQuery;

@Protocol("test")
public class ServerProtocol
{
    @Control
    public GeodeQuery ping(String message)
    {
        System.out.println("Receive from client: " + message);
        return GeodeQuery.simple("pong").pack(message);
    }

    @OnEvent(OnEvent.Event.QUERY_IN)
    public void manage_query(GeodeQuery query)
    {
        System.out.println("receive: " + query);
    }
}
