package com.geode.builders;

import com.geode.annotations.Attribute;
import com.geode.net.SettingsInfos;

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

    @Attribute("enable-logging")
    public SettingsInfosBuilder enableLogging(boolean enableLogging)
    {
        object.setEnableLogging(enableLogging);
        return this;
    }

    @Attribute("append-logging")
    public SettingsInfosBuilder appendLogging(boolean appendLogging)
    {
        object.setAppendLogging(appendLogging);
        return this;
    }

    @Attribute("logging-file")
    public SettingsInfosBuilder loggingFile(String loggingFile)
    {
        object.setLoggingFile(loggingFile);
        return this;
    }
}
