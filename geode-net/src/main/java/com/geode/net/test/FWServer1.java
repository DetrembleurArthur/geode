package com.geode.net.test;

import com.geode.net.Geode;
import com.geode.net.annotations.Protocol;

@Protocol
public class FWServer1
{
    public static void main(String[] args) throws Exception
    {
        if(System.getProperty("log4j.configurationFile") == null)
            System.setProperty("log4j.configurationFile", "geode/log4j.xml");
        Geode.load().launchServer("fw1");
    }
}
