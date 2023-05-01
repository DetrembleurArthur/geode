package com.geode.net.examples;

import com.geode.net.communications.UdpJsonPipe;
import com.geode.net.connections.MulticastGroupConnection;
import com.geode.net.connections.UdpSimpleConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class MulticastDataTransfertReceiver
{
    public static void main(String[] args) throws IOException, ParseException
    {
        MulticastGroupConnection groupConnection = MulticastGroupConnection.receiver("239.0.0.1", 60000);
        UdpJsonPipe jsonPipe = new UdpJsonPipe(groupConnection, false);

        groupConnection.join();

        JSONObject jsonObject;
        jsonObject = jsonPipe.recv();
        System.out.println(jsonObject.toJSONString());
        jsonPipe.send(jsonObject);

        groupConnection.leave();

        jsonPipe.close();
    }
}
