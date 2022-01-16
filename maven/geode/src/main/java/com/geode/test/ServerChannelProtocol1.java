package com.geode.test;

import com.geode.annotations.Control;
import com.geode.annotations.Protocol;
import com.geode.net.GeodeQuery;

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
