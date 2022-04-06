package com.geode.net.conf.info;

import com.geode.net.conf.Attribute;
import com.geode.net.conf.AttributeHolder;

@AttributeHolder
public class ServerInfo
{
    private String host;
    private long port;
    private long backlog;

    public String getHost()
    {
        return host;
    }

    @Attribute("host")
    public void setHost(String host)
    {
        this.host = host;
    }

    public long getPort()
    {
        return port;
    }

    @Attribute("port")
    public void setPort(long port)
    {
        this.port = port;
    }

    public long getBacklog()
    {
        return backlog;
    }

    @Attribute("backlog")
    public void setBacklog(long backlog)
    {
        this.backlog = backlog;
    }

    @Override
    public String toString()
    {
        return "ServerInfo{" +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", backlog=" + backlog +
                '}';
    }
}
