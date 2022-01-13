package com.geode.net;

import com.geode.net.tls.TLSInfos;

/**
 * The type Client infos.
 */
public class ClientInfos extends TLSInfos
{
    private String name;
    private String host;
    private int port;
    private Class<?> protocolClass;
    private boolean enableDiscovery;

    /**
     * Instantiates a new Client infos.
     *
     * @param host          the host
     * @param port          the port
     * @param protocolClass the protocol class
     */
    public ClientInfos(String host, int port, Class<?> protocolClass)
    {
        this.host = host;
        this.port = port;
        this.protocolClass = protocolClass;
        enableDiscovery = true;
    }

    /**
     * Instantiates a new Client infos.
     */
    public ClientInfos()
    {

    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost()
    {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(String host)
    {
        this.host = host;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * Gets protocol class.
     *
     * @return the protocol class
     */
    public Class<?> getProtocolClass()
    {
        return protocolClass;
    }

    /**
     * Sets protocol class.
     *
     * @param protocolClass the protocol class
     */
    public void setProtocolClass(Class<?> protocolClass)
    {
        this.protocolClass = protocolClass;
    }

    public boolean isEnableDiscovery()
    {
        return enableDiscovery;
    }

    public void setEnableDiscovery(boolean enableDiscovery)
    {
        this.enableDiscovery = enableDiscovery;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClientInfos [enableDiscovery=" + enableDiscovery + ", host=" + host + ", name=" + name + ", port="
                + port + ", protocolClass=" + protocolClass + "]";
    }

    
}
