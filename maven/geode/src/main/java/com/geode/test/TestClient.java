package com.geode.test;

import com.geode.net.ClientInfos;
import com.geode.net.ClientProtocolHandler;
import com.geode.net.Geode;
import com.geode.net.GeodeQuery;

public class TestClient
{
    public static void main(String[] args) throws ClassNotFoundException
    {
        Geode geode = new Geode();
        geode.registerClient("myClient", new ClientInfos("127.0.0.1", 50000, Class.forName("com.geode.test.ClientProtocol")));

        ClientProtocolHandler handler = geode.launchClient("myClient").getHandlerSafe();
        handler.send(GeodeQuery.simple("ping").pack("It works!"));
    }
}
