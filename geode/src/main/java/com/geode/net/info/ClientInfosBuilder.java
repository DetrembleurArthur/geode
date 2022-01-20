package com.geode.net.info;

import com.geode.net.annotations.Attribute;
import com.geode.net.channels.ChannelsManager;
import com.geode.net.tls.TLSInfos;

public class ClientInfosBuilder extends Builder<ClientInfos>
{
    static
    {
        BuildersMap.register(ClientInfos.class, ClientInfosBuilder.class);
    }

    public ClientInfosBuilder()
    {
        reset();
    }

    public static ClientInfosBuilder create()
    {
        return new ClientInfosBuilder();
    }

    @Override
    public ClientInfosBuilder reset()
    {
        object = new ClientInfos();
        return this;
    }

    @Attribute("tls")
    public ClientInfosBuilder tlsInfos(TLSInfos tlsInfos)
    {
        object.setTlsInfos(tlsInfos);
        return this;
    }

    @Attribute("name")
    public ClientInfosBuilder name(String name)
    {
        object.setName(name);
        return this;
    }

    @Attribute("host")
    public ClientInfosBuilder host(String host)
    {
        object.setHost(host);
        return this;
    }

    @Attribute("port")
    public ClientInfosBuilder port(int port)
    {
        object.setPort(port);
        return this;
    }

    @Attribute("protocol")
    public ClientInfosBuilder protocolClass(String protocolClass)
    {
        try {
            object.setProtocolClass(Class.forName(protocolClass));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }
    
    @Attribute("discovery")
    public ClientInfosBuilder enableDiscovery(boolean enableDiscovery)
    {
        object.setEnableDiscovery(enableDiscovery);
        return this;
    }

    @Attribute("light")
    public ClientInfosBuilder light(boolean light)
    {
        object.setLight(light);
        return this;
    }

    public ClientInfosBuilder channelsManager(ChannelsManager channelsManager)
    {
        object.setChannelsManager(channelsManager);
        return this;
    }

    @Attribute("channels")
    public ClientInfosBuilder channelsManagerInfos(ChannelsManagerInfos channelsManagerInfos)
    {
        object.setChannelsManagerInfos(channelsManagerInfos);
        return this;
    }

    @Attribute("com-mode")
    public ClientInfosBuilder communicationMode(String communicationMode)
    {
        object.setCommunicationMode(CommunicationModes.valueOf(communicationMode.toUpperCase()));
        return this;
    }
}
