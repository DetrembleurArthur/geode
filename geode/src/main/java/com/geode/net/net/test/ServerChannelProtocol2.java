package com.geode.net.net.test;

import com.geode.net.net.annotations.Control;
import com.geode.net.net.annotations.Protocol;
import com.geode.net.net.GeodeQuery;

@Protocol("p2")
public class ServerChannelProtocol2
{
    @Control
    public Object square(Integer number)
    {
        return GeodeQuery.success("square").pack(number*number);
    }
}
