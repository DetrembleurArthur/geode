package com.geode.net.net.test;

import com.geode.net.net.ClientInfos;
import com.geode.net.net.ClientProtocolHandler;
import com.geode.net.net.Geode;
import com.geode.net.net.GeodeQuery;

public class TestClient
{
    public static void main(String[] args) throws ClassNotFoundException
    {
        Geode geode = new Geode();
        geode.getChannelsManager().createChannel("geode").set(geode);
        ClientInfos infos = new ClientInfos("127.0.0.1", 50000, Class.forName("com.geode.net.net.test.ClientProtocol"));
        geode.registerClient("myClient", infos);

        ClientProtocolHandler handler = geode.launchClient("myClient").getHandlerSafe();
        handler.send(GeodeQuery.simple("ping").pack("It works!"));
    }
}
