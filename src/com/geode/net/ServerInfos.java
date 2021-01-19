package com.geode.net;

public class ServerInfos
{
    private String host;
    private int backlog;
    private int port;

    public ServerInfos(String host, int backlog, int port)
    {
        this.host = host;
        this.backlog = backlog;
        this.port = port;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public int getBacklog()
    {
        return backlog;
    }

    public void setBacklog(int backlog)
    {
        this.backlog = backlog;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    @Override
    public String toString()
    {
        return "ServerInfos{" +
                "host='" + host + '\'' +
                ", backlog=" + backlog +
                ", port=" + port +
                '}';
    }
}
