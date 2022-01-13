package com.geode.test;

import com.geode.net.Geode;
import com.geode.net.GeodeQuery;
import com.geode.net.LightClient;

public class TestClient2
{
    public static void main(String[] args) throws Exception
    {
        LightClient client = Geode.load("src/main/resources/conf2.yml").launchLightClient("myClient");
        client.send(GeodeQuery.simple("ping").pack("hi!"));
        client.recv();
        Thread.sleep(10000);
    }
}
