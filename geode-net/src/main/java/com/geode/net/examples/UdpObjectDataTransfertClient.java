package com.geode.net.examples;

import com.geode.net.communications.UdpObjectPipe;
import com.geode.net.connections.UdpConnection;
import com.geode.net.connections.UdpSimpleConnection;

public class UdpObjectDataTransfertClient
{
    public static void main(String[] args) throws Exception
    {
        UdpSimpleConnection client =  UdpSimpleConnection.client("127.0.0.1", 5000);

        UdpObjectPipe clientPipe = new UdpObjectPipe(client, false);


        TcpObjectDataTransfertServer.Person person = new TcpObjectDataTransfertServer.Person("Arthur", 23);

        for(int i = 0; i < 100; i++)
        clientPipe.send(person);
    }
}
