package com.geode.net.test;

import com.geode.net.Geode;

public class ServerEcho
{
    public static void main(String[] args) throws Exception
    {
        if(System.getProperty("log4j.configurationFile") == null)
            System.setProperty("log4j.configurationFile", "geode/log4j.xml");
        Geode geode = Geode.load();
        geode.launchServer("scenario");
    }
}
