package com.geode.net.examples.protocols;

import java.util.ArrayList;

import com.geode.net.communications.TcpJsonPipe;
import com.geode.net.connections.TcpStickyConnection;
import com.geode.net.handlers.ProtocolHandler;
import com.geode.net.protocols.ProtocolsLoader;

public class Server {
    public static void main(String[] args) throws Exception {
        ProtocolsLoader.load("com.geode.net.examples.protocols");

        TcpStickyConnection connection = TcpStickyConnection.internal(6000, 5);
        TcpJsonPipe jsonPipe = new TcpJsonPipe(connection.accept());

        ProtocolHandler handler = new ProtocolHandler(jsonPipe, "echo");
        handler.run();
    }
}
