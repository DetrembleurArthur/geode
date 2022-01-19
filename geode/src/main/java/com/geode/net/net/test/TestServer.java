package com.geode.net.net.test;

import com.geode.net.net.Geode;
import com.geode.net.net.ServerInfos;

public class TestServer
{
    public static void main(String[] args) throws ClassNotFoundException
    {
        Geode geode = new Geode();
        geode.registerServer("testServer", new ServerInfos("127.0.0.1", 50000, Class.forName("com.geode.net.net.test.ServerProtocol")));
        geode.launchServer("testServer");
    }
}
