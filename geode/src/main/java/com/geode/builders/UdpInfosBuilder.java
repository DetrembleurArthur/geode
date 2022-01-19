package com.geode.builders;

import com.geode.annotations.Attribute;
import com.geode.net.UdpInfos;

public class UdpInfosBuilder extends Builder<UdpInfos>
{
    static
    {
        BuildersMap.register(UdpInfos.class, UdpInfosBuilder.class);
    }

    public UdpInfosBuilder()
    {
        reset();
    }

    public static UdpInfosBuilder create()
    {
        return new UdpInfosBuilder();
    }

    @Override
    public UdpInfosBuilder reset()
    {
        object = new UdpInfos();
        return this;
    }

    @Attribute("bind")
    public UdpInfosBuilder bind(Boolean bind)
    {
        object.setBind(bind);
        return this;
    }

    @Attribute("host")
    public UdpInfosBuilder host(String host)
    {
        object.setHost(host);
        return this;
    }

    @Attribute("name")
    public UdpInfosBuilder name(String name)
    {
        object.setName(name);
        return this;
    }

    @Attribute("port")
    public UdpInfosBuilder port(int port)
    {
        object.setPort(port);
        return this;
    }
}
