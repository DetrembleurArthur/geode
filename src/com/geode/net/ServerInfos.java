package com.geode.net;

import java.util.ArrayList;
import java.util.Arrays;

public class ServerInfos
{
    private String host;
    private int backlog;
    private int port;
    private ArrayList<Class<?>> protocolClasses;

    public ServerInfos(String host, int backlog, int port, Class<?> ... protocolClasses)
    {
        this.host = host;
        this.backlog = backlog;
        this.port = port;
        this.protocolClasses = new ArrayList<>(Arrays.asList(protocolClasses));
    }

    public ServerInfos(String host, int port, Class<?> ... protocolClasses)
    {
        this(host, 50, port, protocolClasses);
    }

    public ServerInfos()
    {

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

    public ArrayList<Class<?>> getProtocolClasses()
    {
        return protocolClasses;
    }

    public void setProtocolClasses(ArrayList<Class<?>> protocolClasses)
    {
        this.protocolClasses = protocolClasses;
    }

    @Override
    public String toString()
    {
        return "ServerInfos{" +
                "host='" + host + '\'' +
                ", backlog=" + backlog +
                ", port=" + port +
                ", protocolClasses=" + protocolClasses +
                '}';
    }
}
