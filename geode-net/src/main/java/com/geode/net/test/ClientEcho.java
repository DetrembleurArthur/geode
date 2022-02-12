package com.geode.net.test;

import com.geode.net.Client;
import com.geode.net.Geode;
import com.geode.net.queries.GeodeQuery;

public class ClientEcho
{
    public static void main(String[] args) throws Exception
    {
        if(System.getProperty("log4j.configurationFile") == null)
            System.setProperty("log4j.configurationFile", "geode/log4j.xml");
        for(int i = 0; i < 1; i++)
        {
            Client client = Geode.load().launchClient("echo");
            client.getHandlerSafe().send(GeodeQuery.simple("echo").pack("Hello world!", 10));
            Thread.sleep(1000);
        }
    }
}
