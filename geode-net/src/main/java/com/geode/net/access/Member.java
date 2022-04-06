package com.geode.net.access;

import com.geode.net.access.Unconnection;

import java.net.MulticastSocket;

public class Member extends Unconnection
{
    public Member()
    {
        super();
    }

    public MulticastSocket getMulticastSocket()
    {
        return (MulticastSocket) socket;
    }

    public void setMulticastSocket(MulticastSocket socket)
    {
        this.socket = socket;
    }
}
