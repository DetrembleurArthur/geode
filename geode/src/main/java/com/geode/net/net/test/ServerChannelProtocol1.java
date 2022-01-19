package com.geode.net.net.test;

import com.geode.net.net.annotations.Control;
import com.geode.net.net.annotations.Protocol;
import com.geode.net.net.GeodeQuery;

@Protocol("p1")
public class ServerChannelProtocol1
{
    @Control
    public Object hello()
    {
        System.out.println("Hello received");
        return GeodeQuery.simple("soso");
    }
}
