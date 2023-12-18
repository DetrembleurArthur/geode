package com.geode.net.arch;

import com.geode.net.conf.ClientConfiguration;
import com.geode.net.connections.Connection;
import com.geode.net.connections.TcpConnection;
import com.geode.net.connections.UdpSimpleConnection;

import java.io.IOException;

public class Client implements Runnable {
    private final ClientConfiguration configuration;
    private Connection connection;

    public Client(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    private void init() throws IOException {
        connection = configuration.isConnectivity() ?
                TcpConnection.on(configuration.getHost(), configuration.getPort()) :
                UdpSimpleConnection.on(configuration.getHost(), configuration.getPort());
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
