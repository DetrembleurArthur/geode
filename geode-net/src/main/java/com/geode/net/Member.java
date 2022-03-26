package com.geode.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
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
