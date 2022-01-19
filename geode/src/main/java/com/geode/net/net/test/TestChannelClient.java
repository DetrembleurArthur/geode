package com.geode.net.net.test;

import com.geode.net.net.Geode;

public class TestChannelClient
{
    public static void main(String[] args) throws Exception
    {
        Geode geode = Geode.load("src/main/resources/channels.yml");
        geode.launchClient("cli1");    
    }    
}
