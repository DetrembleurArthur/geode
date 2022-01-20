package com.geode.net.info;

/**
 * The type Udp infos.
 */
public class UdpInfos
{
    private String name;
    private String host = "127.0.0.1";
    private int port = 50001;
    private boolean bind = false;

    /**
     * Instantiates a new Udp infos.
     */
    public UdpInfos()
    {
        
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "UdpInfos{" +
                "name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", bind=" + bind +
                '}';
    }
}
