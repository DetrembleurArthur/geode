package com.geode.configurations;

public class ProtocolConfigurations implements Configurations
{
    private String name;
    private Class<?> protocolClass;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Class<?> getProtocolClass()
    {
        return protocolClass;
    }

    public void setProtocolClass(Class<?> protocolClass)
    {
        this.protocolClass = protocolClass;
    }
}
