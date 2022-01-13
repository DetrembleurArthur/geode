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
        ClientInfos infos = new ClientInfos("127.0.0.1", 50000, Class.forName("com.geode.test.ClientProtocol"));
        geode.registerClient("myClient", infos);

        ClientProtocolHandler handler = geode.launchClient("myClient").getHandler();
        handler.send(GeodeQuery.simple("ping").pack("It works!"));
    }
}
