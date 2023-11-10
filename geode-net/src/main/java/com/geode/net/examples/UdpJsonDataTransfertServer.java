package com.geode.net.examples;

import com.geode.net.communications.UdpJsonPipe;
import com.geode.net.connections.UdpSimpleConnection;
import org.json.simple.JSONObject;

public class UdpJsonDataTransfertServer
{
    public static void main(String[] args) throws Exception
    {
        UdpSimpleConnection server = UdpSimpleConnection.internal(5000);

        UdpJsonPipe serverPipe = new UdpJsonPipe(server, true);

        while(true)
        {
            JSONObject jsonObject = serverPipe.recv();
            System.out.println(jsonObject.toJSONString());

        }

    }
}
