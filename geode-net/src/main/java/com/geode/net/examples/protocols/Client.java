package com.geode.net.examples.protocols;

import java.io.IOException;
import java.util.ArrayList;

import com.geode.net.communications.TcpOJsonPipe;
import com.geode.net.connections.TcpConnection;
import com.geode.net.handlers.ProtocolHandler;
import com.geode.net.protocols.Protocol;
import com.geode.net.query.Query;

public class Client {

    
    
    public static void main(String[] args) throws Exception {
        TcpConnection connection = TcpConnection.internal(6000);
        TcpOJsonPipe jsonPipe = new TcpOJsonPipe(connection);
        ArrayList<Class<?>> protocolClasses = new ArrayList<>();
        protocolClasses.add(EchoClient.class);

        ProtocolHandler handler = new ProtocolHandler(jsonPipe, protocolClasses);
        Query.Simple("echo").add(20).add("hi").send(jsonPipe);
        handler.run();
    }
}
