package com.geode.net;

public class UdpInfos
{
    private String host;
    private int port;
    private boolean bind;

    public UdpInfos()
    {
        this("127.0.0.1", 5000, false);
    }

    public UdpInfos(String host, int port, boolean bind)
    {
        this.host = host;
        this.port = port;
        this.bind = bind;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public boolean isBind()
    {
        return bind;
    }

    public void setBind(boolean bind)
    {
        this.bind = bind;
    }

    @Override
    public String toString()
    {
        return "UdpInfos{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", bind=" + bind +
                '}';
    }
}
