package com.geode.net.examples;

import com.geode.net.communications.UdpJsonPipe;
import com.geode.net.connections.UdpConnection;
import com.geode.net.connections.UdpSimpleConnection;
import org.json.simple.JSONObject;

public class UdpJsonDataTransfertClient
{
    public static void main(String[] args) throws Exception
    {
        UdpSimpleConnection client =  UdpSimpleConnection.client("127.0.0.1", 5000);

        UdpJsonPipe clientPipe = new UdpJsonPipe(client, false);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Arthur");
        jsonObject.put("age", 23);

        for(int i = 0; i < 100; i++)
        clientPipe.send(jsonObject);
    }
}
