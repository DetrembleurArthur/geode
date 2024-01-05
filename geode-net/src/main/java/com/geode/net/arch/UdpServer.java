package com.geode.net.arch;

import com.geode.net.communications.Pipe;
import com.geode.net.communications.PipeFactory;
import com.geode.net.conf.ServerConfiguration;
import com.geode.net.connections.UdpSimpleConnection;

public class UdpServer extends AbstractServer<UdpSimpleConnection> {

    private Pipe pipe;
    private PipeHandler handler;
    public UdpServer(ServerConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void init() throws Exception {
        connection = UdpSimpleConnection.on(configuration.getHost(), configuration.getPort());
        pipe = PipeFactory.create(configuration.getMode(), false, connection, true);
        handler = new PipeHandler(connection, pipe, configuration.getProtocols());
    }

    @Override
    public void run() {
        handler.start();
    }
}
