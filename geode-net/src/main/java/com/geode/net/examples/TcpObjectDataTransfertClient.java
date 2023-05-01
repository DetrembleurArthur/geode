package com.geode.net.examples;

import com.geode.net.communications.TcpObjectPipe;
import com.geode.net.connections.TcpConnection;

public class TcpObjectDataTransfertClient
{
    public static void main(String[] args) throws Exception
    {
        TcpConnection client = TcpConnection.internal(5000);

        TcpObjectPipe pipe = new TcpObjectPipe(client);

        TcpObjectDataTransfertServer.Person person = new TcpObjectDataTransfertServer.Person("Arthur", 23);
        pipe.send(person);
    }
}
