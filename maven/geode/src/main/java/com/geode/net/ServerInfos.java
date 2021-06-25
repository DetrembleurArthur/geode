package com.geode.net;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The type Server infos.
 */
public class ServerInfos
{
    private String host;
    private int backlog;
    private int port;
    private ArrayList<Class<?>> protocolClasses;

    /**
     * Instantiates a new Server infos.
     *
     * @param host            the host
     * @param backlog         the backlog
     * @param port            the port
     * @param protocolClasses the protocol classes
     */
    public ServerInfos(String host, int backlog, int port, Class<?>... protocolClasses)
    {
        this.host = host;
        this.backlog = backlog;
        this.port = port;
        this.protocolClasses = new ArrayList<>(Arrays.asList(protocolClasses));
    }

    /**
     * Instantiates a new Server infos.
     *
     * @param host            the host
     * @param port            the port
     * @param protocolClasses the protocol classes
     */
    public ServerInfos(String host, int port, Class<?>... protocolClasses)
    {
        this(host, 50, port, protocolClasses);
    }

    /**
     * Instantiates a new Server infos.
     */
    public ServerInfos()
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
     * Gets backlog.
     *
     * @return the backlog
     */
    public int getBacklog()
    {
        return backlog;
    }

    /**
     * Sets backlog.
     *
     * @param backlog the backlog
     */
    public void setBacklog(int backlog)
    {
        this.backlog = backlog;
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
     * Gets protocol classes.
     *
     * @return the protocol classes
     */
    public ArrayList<Class<?>> getProtocolClasses()
    {
        return protocolClasses;
    }

    /**
     * Sets protocol classes.
     *
     * @param protocolClasses the protocol classes
     */
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
