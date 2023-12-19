package com.geode.net.arch;

import com.geode.net.communications.Pipe;
import com.geode.net.conf.ServerConfiguration;
import com.geode.net.connections.Connection;

import java.io.IOException;
import java.util.ArrayList;

public abstract class AbstractServer<T extends Connection> implements Runnable {

    protected final ServerConfiguration configuration;
    protected T connection;
    protected ArrayList<PipeHandler> pipeHandlers;
    protected boolean running = false;

    public AbstractServer(ServerConfiguration configuration)
    {
        this.configuration = configuration;
        pipeHandlers = new ArrayList<>();
    }

    protected abstract void init() throws Exception;

}
