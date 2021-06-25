package com.geode.test;

import com.geode.net.ClientInfos;
import com.geode.net.Geode;
import com.geode.net.Query;

public class Client
{
    public static void main(String[] args) throws Exception
    {
        Geode geode = new Geode();
        geode.init("src/main/resources/conf.yml");
        geode.launchClient("pingClient").getHandlerSafe().send(Query.simple("ping"));
    }
}
