package com.geode.net.test;

import com.geode.crypto.Keys;
import com.geode.crypto.Sign;
import com.geode.net.Geode;
import com.geode.net.annotations.Control;
import com.geode.net.annotations.Protocol;
import com.geode.net.queries.GeodeQuery;

import java.security.KeyPair;

@Protocol
public class FWServer2
{
    @Control
    public GeodeQuery sign(String message)
    {
        KeyPair pair = Keys.rsa();
        Sign sign = Sign.sha1WithRsa(pair);
        byte[] signed = sign.signMode().feed(message.getBytes()).sign();
        System.out.println("message signed: " + message);
        return GeodeQuery.simple("response").pack(signed, pair.getPublic());
    }

    public static void main(String[] args) throws Exception
    {
        if(System.getProperty("log4j.configurationFile") == null)
            System.setProperty("log4j.configurationFile", "geode/log4j.xml");
        Geode.load().launchServer("fw2");
    }
}
