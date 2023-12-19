package com.geode.net.arch;

import com.geode.net.communications.Pipe;
import com.geode.net.communications.PipeFactory;
import com.geode.net.conf.ClientConfiguration;
import com.geode.net.connections.Connection;
import com.geode.net.connections.TcpConnection;
import com.geode.net.connections.UdpSimpleConnection;

import java.io.IOException;

public class Client {
    private final ClientConfiguration configuration;
    private Connection connection;
    private Pipe pipe;
    private PipeHandler handler;

    public Client(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public void init() throws Exception {
        if(connection == null || !pipe.available())
        {
            connection = configuration.isConnectivity() ?
                    TcpConnection.on(configuration.getHost(), configuration.getPort()) :
                    UdpSimpleConnection.client(configuration.getHost(), configuration.getPort());
            pipe = PipeFactory.create(configuration.getMode(), configuration.isConnectivity(), connection);
            handler = new PipeHandler(connection, pipe, configuration.getProtocols());
            handler.start();
        }
    }

    public Pipe getPipe() {
        return pipe;
    }
}
