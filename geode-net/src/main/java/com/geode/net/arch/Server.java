package com.geode.net.arch;

import com.geode.net.conf.ServerConfiguration;
import com.geode.net.connections.Connection;
import com.geode.net.connections.TcpConnection;
import com.geode.net.connections.TcpStickyConnection;
import com.geode.net.connections.UdpSimpleConnection;
import com.geode.net.handlers.ProtocolHandler;

import java.io.IOException;

public class Server implements Runnable {

    private final ServerConfiguration configuration;
    private Connection connection;
    private boolean running = false;

    public Server(ServerConfiguration configuration)
    {
        this.configuration = configuration;
    }

    private void init() throws IOException {
        connection = configuration.isConnectivity() ?
                TcpStickyConnection.on(configuration.getHost(), configuration.getPort(), 5) :
                UdpSimpleConnection.on(configuration.getHost(), configuration.getPort());
    }

    @Override
    public void run() {
        try {
            init();
            running = true;
            while(running)
            {
                // create thread here
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
