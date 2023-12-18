package com.geode.net.conf;

public class ServerConfiguration {
    private boolean connectivity = true;
    private String host = "127.0.0.1";
    private int port = 50000;
    private int backlog = 5;

    public ServerConfiguration() {
    }

    public boolean isConnectivity() {
        return connectivity;
    }

    public ServerConfiguration setConnectivity(boolean connectivity) {
        this.connectivity = connectivity;
        return this;
    }

    public String getHost() {
        return host;
    }

    public ServerConfiguration setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ServerConfiguration setPort(int port) {
        this.port = port;
        return this;
    }

    public int getBacklog() {
        return backlog;
    }

    public ServerConfiguration setBacklog(int backlog) {
        this.backlog = backlog;
        return this;
    }
}
