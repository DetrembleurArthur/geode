package com.geode.test;

import com.geode.net.Geode;

public class TestServer2
{
    public static void main(String[] args) throws Exception
    {
        Geode.load("src/main/resources/conf2.yml").launchServer("myServer");    
    }    
}
