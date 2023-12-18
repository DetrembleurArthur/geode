package com.geode.net.examples;

import com.geode.net.communications.UdpJsonPipe;
import com.geode.net.connections.MulticastGroupConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class MulticastDataTransfertReceiver
{// to rework
    public static void main(String[] args) throws Exception {
        MulticastGroupConnection groupConnection = MulticastGroupConnection.receiver("239.0.0.1", 60000);
        UdpJsonPipe jsonPipe = new UdpJsonPipe(groupConnection, false);

        groupConnection.join();

        jsonPipe.prepareRecv(TcpJsonObjDataTransfert.Person.class);
        TcpJsonObjDataTransfert.Person person = (TcpJsonObjDataTransfert.Person) jsonPipe.recv();
        System.out.println(person.toString());
        jsonPipe.send(person);

        groupConnection.leave();

        jsonPipe.close();
    }
}
