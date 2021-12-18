package com.geode.test;

import com.geode.annotations.Control;
import com.geode.annotations.Protocol;

@Protocol("test")
public class ClientProtocol
{
    @Control
    public void pong(String message)
    {
        System.out.println("Receive from server: " + message);
    }
}
