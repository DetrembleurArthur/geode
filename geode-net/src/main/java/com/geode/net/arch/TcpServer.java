package com.geode.net.arch;

import com.geode.net.communications.Pipe;
import com.geode.net.communications.PipeFactory;
import com.geode.net.conf.ServerConfiguration;
import com.geode.net.connections.TcpConnection;
import com.geode.net.connections.TcpStickyConnection;

import java.io.IOException;

public class TcpServer extends AbstractServer<TcpStickyConnection> {

    public TcpServer(ServerConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void init() throws IOException {
        connection = TcpStickyConnection.on(configuration.getHost(), configuration.getPort(), 5);
    }

    @Override
    public void run() {
        running = true;
        while(running)
        {
            try {
                TcpConnection clientConnection = connection.accept();
                Pipe pipe = PipeFactory.create(configuration.getMode(), true, clientConnection);
                PipeHandler pipeHandler = new PipeHandler(clientConnection, pipe, configuration.getProtocols());
                pipeHandlers.add(pipeHandler);
                pipeHandler.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
