package com.geode.net.examples.protocols;

import java.util.ArrayList;

import com.geode.net.communications.TcpJsonPipe;
import com.geode.net.connections.TcpConnection;
import com.geode.net.handlers.ProtocolHandler;
import com.geode.net.protocols.ProtocolsLoader;
import com.geode.net.query.Query;

public class Client {

    
    
    public static void main(String[] args) throws Exception {
        ProtocolsLoader.load("com.geode.net.examples.protocols");
        TcpConnection connection = TcpConnection.internal(6000);
        TcpJsonPipe jsonPipe = new TcpJsonPipe(connection);

        ProtocolHandler handler = new ProtocolHandler(jsonPipe, "echo");
        Query.Simple("echo").add(20).add("hi").send(jsonPipe);
        handler.run();
    }
}
