package com.geode.net.examples.protocols;

import java.io.IOException;

import com.geode.net.communications.Mode;
import com.geode.net.communications.TcpJsonPipe;
import com.geode.net.conf.ServerConfiguration;
import com.geode.net.connections.TcpStickyConnection;
import com.geode.net.handlers.ProtocolHandler;
import com.geode.net.protocols.ProtocolsLoader;

public class TcpServer {
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

    public static void main(String[] args) throws IOException {
        com.geode.net.arch.TcpServer server = new com.geode.net.arch.TcpServer(new ServerConfiguration()
                .setHost("127.0.0.1")
                .setPort(6000)
                .setBacklog(5)
                .setMode(Mode.OBJ)
                .setProtocols(new String[]{"echo"})
                .setConnectivity(true));
        server.init();
        server.run();
    }
}
