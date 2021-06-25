package com.geode.test;

import com.geode.net.ClientInfos;
import com.geode.net.Geode;
import com.geode.net.Query;

public class Client
{
    public static void main(String[] args)
    {
        Geode geode = new Geode();
        geode.registerClient("MyClient", new ClientInfos("127.0.0.1", 50000, Ping.class));
        geode.launchClient("MyClient").getHandlerSafe().send(Query.simple("ping"));
    }
}
