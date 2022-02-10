package com.geode.net.info;

import java.util.ArrayList;

import com.geode.net.annotations.Attribute;
import com.geode.net.channels.ChannelsManager;
import com.geode.net.tls.TLSInfos;

public class ServerInfosBuilder extends Builder<ServerInfos>
{   
    static
    {
        BuildersMap.register(ServerInfos.class, ServerInfosBuilder.class);
    }

    public ServerInfosBuilder()
    {
        reset();
    }

    public static ServerInfosBuilder create()
    {
        return new ServerInfosBuilder();
    }

    @Override
    public ServerInfosBuilder reset()
    {
        object = new ServerInfos();
        return this;
    }

    @Attribute("tls")
    public ServerInfosBuilder tlsInfos(TLSInfos tlsInfos)
    {
        object.setTlsInfos(tlsInfos);
        return this;
    }

    @Attribute("name")
    public ServerInfosBuilder name(String name)
    {
        object.setName(name);
        return this;
    }

    @Attribute("host")
    public ServerInfosBuilder host(String host)
    {
        object.setHost(host);
        return this;
    }

    @Attribute("port")
    public ServerInfosBuilder port(int port)
    {
        object.setPort(port);
        return this;
    }

    @Attribute("protocols")
    public ServerInfosBuilder protocolClasses(ArrayList<String> protocolClasses)
    {
        ArrayList<Class<?>> classes = new ArrayList<>();
        for(String classname : protocolClasses)
            try {
                classes.add(Class.forName(classname));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        object.setProtocolClasses(classes);
        return this;
    }
    
    @Attribute("discovery")
    public ServerInfosBuilder enableDiscovery(boolean enableDiscovery)
    {
        object.setEnableDiscovery(enableDiscovery);
        return this;
    }

    public ServerInfosBuilder channelsManager(ChannelsManager channelsManager)
    {
        object.setChannelsManager(channelsManager);
        return this;
    }

    @Attribute("channels")
    public ServerInfosBuilder channelsManagerInfos(ChannelsManagerInfos channelsManagerInfos)
    {
        object.setChannelsManagerInfos(channelsManagerInfos);
        return this;
    }

    @Attribute("backlog")
    public ServerInfosBuilder backlog(int backlog)
    {
        object.setBacklog(backlog);
        return this;
    }

    @Attribute("max-handlers")
    public ServerInfosBuilder maxHandlers(int maxHandler)
    {
        object.setMaxHandlers(maxHandler);
        return this;
    }

    @Attribute("banner")
    public ServerInfosBuilder banner(String banner)
    {
        object.setBanner(banner);
        return this;
    }

    @Attribute("com-mode")
    public ServerInfosBuilder communicationMode(String communicationMode)
    {
        object.setCommunicationMode(CommunicationModes.valueOf(communicationMode.toUpperCase()));
        return this;
    }

    @Attribute("filters")
    public ServerInfosBuilder filtersInfos(FiltersInfos filtersInfos)
    {
        object.setFiltersInfos(filtersInfos);
        return this;
    }
}
