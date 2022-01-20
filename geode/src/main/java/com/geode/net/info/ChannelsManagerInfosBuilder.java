package com.geode.net.info;

import com.geode.net.annotations.Attribute;

public class ChannelsManagerInfosBuilder extends Builder<ChannelsManagerInfos>
{
    static
    {
        BuildersMap.register(ChannelsManagerInfos.class, ChannelsManagerInfosBuilder.class);
    }

    public ChannelsManagerInfosBuilder()
    {
        reset();
    }

    public static ChannelsManagerInfosBuilder create()
    {
        return new ChannelsManagerInfosBuilder();
    }

    @Override
    public ChannelsManagerInfosBuilder reset()
    {
        object = new ChannelsManagerInfos();
        return this;
    }

    @Attribute("enable")
    public ChannelsManagerInfosBuilder enable(Boolean enable)
    {
        object.setEnable(enable);
        return this;
    }

    @Attribute("strict")
    public ChannelsManagerInfosBuilder strict(Boolean strict)
    {
        object.setStrict(strict);
        return this;
    }
}
