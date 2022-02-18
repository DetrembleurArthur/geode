package com.geode.net.info;

import java.util.ArrayList;
import java.util.Arrays;

import com.geode.net.channels.ChannelsManager;
import com.geode.net.tls.TLSInfos;

/**
 * The type Server infos.
 */
public class ServerInfos
{
    private TLSInfos tlsInfos = new TLSInfos();
    private String name;
    private String host = "127.0.0.1";
    private int backlog = 10;
    private int port = 50000;
    private boolean enableDiscovery = true;
    private ArrayList<Class<?>> protocolClasses = new ArrayList<>();
    private int maxHandlers = Integer.MAX_VALUE;
    private String banner = null;
    private ChannelsManager channelsManager;
    private ChannelsManagerInfos channelsManagerInfos = new ChannelsManagerInfos();
    private FiltersInfos filtersInfos = new FiltersInfos();
    private CommunicationModes modes = CommunicationModes.OBJECT;

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
        enableDiscovery = true;
        maxHandlers = Integer.MAX_VALUE;
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    

    public boolean isEnableDiscovery()
    {
        return enableDiscovery;
    }

    public void setEnableDiscovery(boolean enableDiscovery)
    {
        this.enableDiscovery = enableDiscovery;
    }

    

    public int getMaxHandlers()
    {
        return maxHandlers;
    }

    public void setMaxHandlers(int maxHandlers)
    {
        this.maxHandlers = maxHandlers;
    }

    public ChannelsManager getChannelsManager() {
        return channelsManager;
    }

    public void setChannelsManager(ChannelsManager channelsManager) {
        this.channelsManager = channelsManager;
    }

    public ChannelsManagerInfos getChannelsManagerInfos() {
        return channelsManagerInfos;
    }

    public void setChannelsManagerInfos(ChannelsManagerInfos channelsManagerInfos) {
        this.channelsManagerInfos = channelsManagerInfos;
    }

    

    public TLSInfos getTlsInfos() {
        return tlsInfos;
    }

    public void setTlsInfos(TLSInfos tlsInfos) {
        this.tlsInfos = tlsInfos;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public CommunicationModes getCommunicationMode()
    {
        return modes;
    }

    public void setCommunicationMode(CommunicationModes modes)
    {
        this.modes = modes;
    }

    public FiltersInfos getFiltersInfos() {
        return filtersInfos;
    }

    public void setFiltersInfos(FiltersInfos filtersInfos) {
        this.filtersInfos = filtersInfos;
    }


    @Override
    public String toString() {
        return "ServerInfos [backlog=" + backlog + ", banner=" + banner + ", channelsManager=" + channelsManager
                + ", channelsManagerInfos=" + channelsManagerInfos + ", enableDiscovery=" + enableDiscovery + ", host="
                + host + ", maxHandlers=" + maxHandlers + ", name=" + name + ", port=" + port + ", protocolClasses="
                + protocolClasses + ", tlsInfos=" + tlsInfos + "]";
    }
}
