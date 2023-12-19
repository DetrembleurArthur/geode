package com.geode.net.arch;

import com.geode.net.communications.Pipe;
import com.geode.net.connections.Connection;
import com.geode.net.handlers.ProtocolHandler;

public class PipeHandler extends Thread {
    private ProtocolHandler ph;
    private Connection connection;

    public PipeHandler(Connection connection, Pipe pipe, String ... protocolNames) throws Exception {
        this.connection = connection;
        this.ph = new ProtocolHandler(pipe, protocolNames);
    }

    @Override
    public void run()
    {
        ph.run();
    }
}
