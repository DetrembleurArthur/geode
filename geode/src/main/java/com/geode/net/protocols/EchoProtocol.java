package com.geode.net.protocols;

import com.geode.net.queries.GeodeQuery;
import com.geode.net.annotations.Control;
import com.geode.net.annotations.Protocol;

import java.io.Serializable;

@Protocol("echo")
public class EchoProtocol
{
    @Control
    public Object echo(Serializable object, Integer ttl)
    {
        System.out.println("" + object + " ttl: " + ttl);
        ttl--;
        return ttl > 0 ? GeodeQuery.simple("echo").pack(object, ttl) : GeodeQuery.NO_QUERY;
    }
}
