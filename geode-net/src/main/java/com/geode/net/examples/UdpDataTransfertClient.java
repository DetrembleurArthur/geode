package com.geode.net.examples;

import com.geode.net.communications.UdpDataPipe;
import com.geode.net.communications.UdpObjectPipe;
import com.geode.net.connections.UdpSimpleConnection;

public class UdpDataTransfertClient
{
    public static void main(String[] args) throws Exception
    {
        UdpSimpleConnection client =  UdpSimpleConnection.client("127.0.0.1", 6000);

        UdpDataPipe clientPipe = new UdpDataPipe(client, false);


        byte[] data = new byte[]{
                (byte) 128, (byte) 255, (byte) 188
        };

        clientPipe.send(data);
    }
}
