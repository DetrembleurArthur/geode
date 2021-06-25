package com.geode.net;

/**
 * The type Udp infos.
 */
public class UdpInfos
{
    private String host;
    private int port;
    private boolean bind;

    /**
     * Instantiates a new Udp infos.
     */
    public UdpInfos()
    {
        this("127.0.0.1", 5000, false);
    }

    /**
     * Instantiates a new Udp infos.
     *
     * @param host the host
     * @param port the port
     * @param bind the bind
     */
    public UdpInfos(String host, int port, boolean bind)
    {
        this.host = host;
        this.port = port;
        this.bind = bind;
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
     * Is bind boolean.
     *
     * @return the boolean
     */
    public boolean isBind()
    {
        return bind;
    }

    /**
     * Sets bind.
     *
     * @param bind the bind
     */
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
