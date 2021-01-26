package com.geode.net;

public class ClientInfos
{
    private String host;
    private int port;
    private Class<?> protocolClass;

    public ClientInfos(String host, int port, Class<?> protocolClass)
    {
        this.host = host;
        this.port = port;
        this.protocolClass = protocolClass;
    }

    public ClientInfos()
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

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public Class<?> getProtocolClass()
    {
        return protocolClass;
    }

    public void setProtocolClass(Class<?> protocolClass)
    {
        this.protocolClass = protocolClass;
    }

    @Override
    public String toString()
    {
        return "ClientInfos{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", protocolClass=" + protocolClass +
                '}';
    }
}
