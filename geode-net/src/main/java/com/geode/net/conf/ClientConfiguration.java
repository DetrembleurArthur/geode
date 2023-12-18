package com.geode.net.conf;

import com.geode.net.communications.Mode;

public class ClientConfiguration {
    private boolean connectivity = true;
    private String host = "127.0.0.1";
    private int port = 50000;
    private Mode mode = Mode.JSON;

    public ClientConfiguration() {
    }

    public boolean isConnectivity() {
        return connectivity;
    }

    public ClientConfiguration setConnectivity(boolean connectivity) {
        this.connectivity = connectivity;
        return this;
    }

    public String getHost() {
        return host;
    }

    public ClientConfiguration setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ClientConfiguration setPort(int port) {
        this.port = port;
        return this;
    }

    public Mode getMode() {
        return mode;
    }

    public ClientConfiguration setMode(Mode mode) {
        this.mode = mode;
        return this;
    }
}
