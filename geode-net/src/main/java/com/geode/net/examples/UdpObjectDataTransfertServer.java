package com.geode.net.examples;

import com.geode.net.communications.UdpObjectPipe;
import com.geode.net.connections.UdpConnection;
import com.geode.net.connections.UdpSimpleConnection;

public class UdpObjectDataTransfertServer
{
    public static void main(String[] args) throws Exception
    {
        UdpSimpleConnection server = UdpSimpleConnection.internal(5000);

        UdpObjectPipe serverPipe = new UdpObjectPipe(server, true);

        while(true)
        {
            TcpObjectDataTransfertServer.Person person = (TcpObjectDataTransfertServer.Person) serverPipe.recv();
            System.out.println(person.toString());

        }

    }
}
