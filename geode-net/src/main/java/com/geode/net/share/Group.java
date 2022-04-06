package com.geode.net.share;

import com.geode.net.access.Member;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Group
{
    private InetAddress address;
    private int port;

    public Group(String group, int port) throws Exception
    {
        address = InetAddress.getByName(group);
        if(!address.isMulticastAddress())
        {
            throw new Exception(address + " is not multicast");
        }
        this.port = port;
    }

    public Member createMember() throws IOException
    {
        Member member = new Member();
        MulticastSocket socket = new MulticastSocket(port);
        member.setMulticastSocket(socket);
        join(member);
        return member;
    }

    public void join(Member member) throws IOException
    {
        member.getMulticastSocket().joinGroup(address);
    }

    public void leave(Member member) throws IOException
    {
        member.getMulticastSocket().leaveGroup(address);
    }

    public DatagramPacket send(Member member, byte[] data) throws IOException
    {
        return member.sendto(data, address, port);
    }
}
