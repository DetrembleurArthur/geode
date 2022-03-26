package com.geode.net;

import java.nio.ByteBuffer;

public class MulticastServer
{
    public static void main(String[] args) throws Exception
    {
        Group group = new Group("230.0.0.1", 20000);
        Member member = group.createMember();
        while(true)
        {
            group.send(member, ByteBuffer.allocate(4).putInt(15000).flip().array());
            Thread.sleep(3000);
        }
    }
}
