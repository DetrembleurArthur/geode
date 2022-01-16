package com.geode.test;

import com.geode.annotations.Control;
import com.geode.annotations.Protocol;
import com.geode.net.GeodeQuery;

@Protocol("p2")
public class ServerChannelProtocol2
{
    @Control
    public Object square(Integer number)
    {
        return GeodeQuery.success("square").pack(number*number);
    }
}
