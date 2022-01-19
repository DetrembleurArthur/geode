package com.geode.net;

import com.geode.net.channels.ChannelsManager;
import com.geode.net.channels.ChannelsManagerInfos;
import com.geode.net.tls.TLSInfos;

/**
 * The type Client infos.
 */
public class ClientInfos
{
    private TLSInfos tlsInfos = new TLSInfos();
    private boolean light = false;
    private String name;
    private String host = "127.0.0.1";
    private int port = 50000;
    private Class<?> protocolClass;
    private boolean enableDiscovery = true;
    private ChannelsManager channelsManager;
    private ChannelsManagerInfos channelsManagerInfos = new ChannelsManagerInfos();

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
        this.enableDiscovery = light ? false : enableDiscovery;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    

    public boolean isLight() {
        return light;
    }

    public void setLight(boolean light) {
        this.light = light;
        setEnableDiscovery(false);
    }

    @Override
    public String toString() {
        return "ClientInfos [channelsManager=" + channelsManager + ", channelsManagerInfos=" + channelsManagerInfos
                + ", enableDiscovery=" + enableDiscovery + ", host=" + host + ", light=" + light + ", name=" + name
                + ", port=" + port + ", protocolClass=" + protocolClass + ", tlsInfos=" + tlsInfos + "]";
    }

    
}
