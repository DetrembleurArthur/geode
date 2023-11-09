package com.geode.net.examples;

import com.geode.net.communications.TcpJsonPipe;
import com.geode.net.connections.TcpConnection;
import com.geode.net.connections.TcpStickyConnection;
import org.json.simple.JSONObject;

public class TcpJsonDataTransfert
{
    public static void main(String[] args) throws Exception
    {
        TcpStickyConnection server = TcpStickyConnection.internal(5000, 5);
        TcpConnection client = TcpConnection.internal(5000);
        TcpConnection clientHandler = server.accept();

        TcpJsonPipe clientPipe = new TcpJsonPipe(client);
        TcpJsonPipe clientHandlerPipe = new TcpJsonPipe(clientHandler);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Arthur");
        jsonObject.put("age", 23);

        clientPipe.send(jsonObject);
        jsonObject = clientHandlerPipe.recv();
        System.out.println(jsonObject.toJSONString());

        clientPipe.close();
        clientHandlerPipe.close();
        server.close();
    }
}
