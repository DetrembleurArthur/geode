package com.geode;

import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.Protocol;
import com.geode.net.GeodeHandler;
import com.geode.net.Query;

@Protocol
public class Server
{
    @Inject
    public GeodeHandler geodeHandler;


    @Control
    public void ping()
    {
        geodeHandler.send(Query.simple("pong").pack("My message"));
    }
}
