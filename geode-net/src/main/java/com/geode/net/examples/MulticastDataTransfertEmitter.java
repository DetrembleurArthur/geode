package com.geode.net.examples;

import com.geode.net.communications.UdpJsonPipe;
import com.geode.net.connections.MulticastGroupConnection;

import java.io.IOException;

public class MulticastDataTransfertEmitter
{// to rework
    public static void main(String[] args) throws IOException
    {
        MulticastGroupConnection emitter = MulticastGroupConnection.emitter("239.0.0.1", 60000);
        UdpJsonPipe jsonPipe = new UdpJsonPipe(emitter, false);

        TcpJsonObjDataTransfert.Person person = new TcpJsonObjDataTransfert.Person("Arthur",  24);

        jsonPipe.send(person);
        jsonPipe.close();
    }
}
