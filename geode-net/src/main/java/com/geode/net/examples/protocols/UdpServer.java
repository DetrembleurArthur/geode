package com.geode.net.examples.protocols;

import com.geode.net.communications.Mode;
import com.geode.net.communications.TcpJsonPipe;
import com.geode.net.conf.ServerConfiguration;
import com.geode.net.connections.TcpStickyConnection;
import com.geode.net.handlers.ProtocolHandler;
import com.geode.net.protocols.ProtocolsLoader;

import java.io.IOException;

public class UdpServer {
    static
    {
        ProtocolsLoader.load("com.geode.net.examples.protocols");
    }
    public static void main1(String[] args) throws Exception {
        ProtocolsLoader.load("com.geode.net.examples.protocols");

        TcpStickyConnection connection = TcpStickyConnection.internal(6000, 5);
        TcpJsonPipe jsonPipe = new TcpJsonPipe(connection.accept());

        ProtocolHandler handler = new ProtocolHandler(jsonPipe, "echo");
        handler.run();
    }

    public static void main(String[] args) throws Exception {
        com.geode.net.arch.UdpServer server = new com.geode.net.arch.UdpServer(new ServerConfiguration()
                .setHost("127.0.0.1")
                .setPort(6000)
                .setMode(Mode.OBJ)
                .setProtocols(new String[]{"echo"})
                .setConnectivity(false));
        server.init();
        server.run();
    }
}
