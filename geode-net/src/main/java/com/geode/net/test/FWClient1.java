package com.geode.net.test;

import com.geode.crypto.Sign;
import com.geode.net.Geode;
import com.geode.net.ProtocolHandler;
import com.geode.net.annotations.Control;
import com.geode.net.annotations.Inject;
import com.geode.net.annotations.OnEvent;
import com.geode.net.annotations.Protocol;
import com.geode.net.queries.GeodeQuery;

import java.security.PublicKey;

@Protocol
public class FWClient1
{
    @Inject
    public ProtocolHandler handler;
    private String message;

    @OnEvent(OnEvent.Event.INIT)
    public void init()
    {
        message = "Hello world!";
        handler.send(
                GeodeQuery.forward().forwarder("127.0.0.1", 50001, true)
                        .pack(GeodeQuery.simple("sign").pack(message)));
    }

    @Control
    public void response(byte[] signedMessage, PublicKey publicKey)
    {
        boolean verify = Sign.sha1WithRsa(publicKey).feed(message.getBytes()).verify(signedMessage);
        if (verify)
        {
            System.out.println("message has been signed successfully");
        } else
        {
            System.out.println("message has not been signed successfully");
        }
    }


    public static void main(String[] args) throws Exception
    {
        if(System.getProperty("log4j.configurationFile") == null)
            System.setProperty("log4j.configurationFile", "geode/log4j.xml");
        Geode.load().launchClient("fw");
    }
}
