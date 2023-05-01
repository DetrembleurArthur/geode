package com.geode.net.examples;

import com.geode.net.communications.UdpJsonPipe;
import com.geode.net.connections.MulticastGroupConnection;
import com.geode.net.connections.UdpSimpleConnection;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.SocketException;

public class MulticastDataTransfertEmitter
{
    public static void main(String[] args) throws IOException
    {
        MulticastGroupConnection emitter = MulticastGroupConnection.emitter("239.0.0.1", 60000);
        UdpJsonPipe jsonPipe = new UdpJsonPipe(emitter, false);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Arthur");
        jsonObject.put("age", 23);

        jsonPipe.send(jsonObject);
        jsonPipe.close();
    }
}
