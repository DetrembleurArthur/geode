package com.geode.net.info;

import com.geode.net.annotations.Attribute;

public class SettingsInfosBuilder extends Builder<SettingsInfos>
{
    static
    {
        BuildersMap.register(SettingsInfos.class, SettingsInfosBuilder.class);
    }

    public SettingsInfosBuilder()
    {
        reset();
    }

    public static SettingsInfosBuilder create()
    {
        return new SettingsInfosBuilder();
    }

    @Override
    public SettingsInfosBuilder reset()
    {
        object = new SettingsInfos();
        return this;
    }

    @Attribute("log4j-location")
    public SettingsInfosBuilder log4jLocation(String log4jLocation)
    {
        object.setLog4jLocation(log4jLocation);
        return this;
    }
}
