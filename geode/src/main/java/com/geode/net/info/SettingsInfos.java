package com.geode.net.info;

public class SettingsInfos
{
    private String log4jLocation = null;

    public String getLog4jLocation()
    {
        return log4jLocation;
    }

    public void setLog4jLocation(String log4jLocation)
    {
        this.log4jLocation = log4jLocation;
    }

    @Override
    public String toString()
    {
        return "SettingsInfos{" +
                "log4jLocation='" + log4jLocation + '\'' +
                '}';
    }
}
