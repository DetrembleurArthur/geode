package com.geode.test;

import com.geode.net.Geode;

public class TestChannelServer
{
    public static void main(String[] args) throws Exception
    {
        Geode.load("src/main/resources/channels.yml").launchServer("srv");    
    }    
}
