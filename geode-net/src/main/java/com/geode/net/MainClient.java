package com.geode.net;

import com.geode.net.access.Connection;
import com.geode.net.access.Member;
import com.geode.net.mgmt.ConnectionHandler;
import com.geode.net.share.Group;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public class MainClient
{
    public static void main(String[] args) throws Exception
    {
        Connection connection = Connection.internal(50000);
        ConnectionHandler handler = new ConnectionHandler(connection);
        handler.runAsThread();
    }
}
