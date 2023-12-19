package com.geode.net.examples.protocols;

import com.geode.net.communications.Mode;
import com.geode.net.communications.TcpJsonPipe;
import com.geode.net.conf.ClientConfiguration;
import com.geode.net.connections.TcpConnection;
import com.geode.net.handlers.ProtocolHandler;
import com.geode.net.protocols.ProtocolsLoader;
import com.geode.net.query.Query;

public class TcpClient {

    static {
        ProtocolsLoader.load("com.geode.net.examples.protocols");
    }
    
    
    public static void main1(String[] args) throws Exception {
        ProtocolsLoader.load("com.geode.net.examples.protocols");
        TcpConnection connection = TcpConnection.internal(6000);
        TcpJsonPipe jsonPipe = new TcpJsonPipe(connection);

        ProtocolHandler handler = new ProtocolHandler(jsonPipe, "echo");
        Query.Simple("echo").add(20).add("hi").send(jsonPipe);
        handler.run();
    }

    public static void main(String[] args) throws Exception {
        com.geode.net.arch.Client client = new com.geode.net.arch.Client(new ClientConfiguration()
                .setHost("127.0.0.1")
                .setPort(6000)
                .setMode(Mode.OBJ)
                .setProtocols(new String[]{"echo"})
                .setConnectivity(true));
        client.init();

        client.getPipe().send(Query.Simple("echo").add(5));
    }
}
