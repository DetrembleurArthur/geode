package com.geode.test;

import com.geode.net.Geode;

public class Server
{
    public static void main(String[] args) throws Exception
    {
        Geode geode = new Geode();
        geode.init("src/main/resources/conf.yml");
        geode.launchServer("pingServer");
    }
}
