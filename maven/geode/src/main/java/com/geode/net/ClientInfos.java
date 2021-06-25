package com.geode.net;

/**
 * The type Client infos.
 */
public class ClientInfos
{
    private String host;
    private int port;
    private Class<?> protocolClass;
    private boolean connectedMode;

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
        connectedMode = true;
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

    /**
     * Is connected mode boolean.
     *
     * @return the boolean
     */
    public boolean isConnectedMode()
    {
        return connectedMode;
    }

    /**
     * Sets connected mode.
     *
     * @param connectedMode the connected mode
     */
    public void setConnectedMode(boolean connectedMode)
    {
        this.connectedMode = connectedMode;
    }

    @Override
    public String toString()
    {
        return "ClientInfos{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", protocolClass=" + protocolClass +
                ", connectedMode=" + connectedMode +
                '}';
    }
}
