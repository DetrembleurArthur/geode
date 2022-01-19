package com.geode.test;

import com.geode.annotations.Control;
import com.geode.annotations.OnEvent;
import com.geode.annotations.Protocol;
import com.geode.net.GeodeQuery;

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
