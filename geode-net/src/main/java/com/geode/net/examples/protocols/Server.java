package com.geode.net.examples.protocols;

import java.util.ArrayList;

import com.geode.net.communications.TcpOJsonPipe;
import com.geode.net.connections.TcpConnection;
import com.geode.net.connections.TcpStickyConnection;
import com.geode.net.handlers.ProtocolHandler;
import com.geode.net.query.Query;

public class Server {
    public static void main(String[] args) throws Exception {
        TcpStickyConnection connection = TcpStickyConnection.internal(6000, 5);
        TcpOJsonPipe jsonPipe = new TcpOJsonPipe(connection.accept());
        ArrayList<Class<?>> protocolClasses = new ArrayList<>();
        protocolClasses.add(EchoClient.class);

        ProtocolHandler handler = new ProtocolHandler(jsonPipe, protocolClasses);
        handler.run();
    }
}
