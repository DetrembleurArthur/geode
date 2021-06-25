package com.geode.test;

import com.geode.net.Geode;
import com.geode.net.ServerInfos;

public class Server
{
    public static void main(String[] args)
    {
        Geode geode = new Geode();
        geode.registerServer("MyServer", new ServerInfos("127.0.0.1", 50000, Pong.class));
        geode.launchServer("MyServer");
    }
}
