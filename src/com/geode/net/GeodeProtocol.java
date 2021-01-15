package com.geode.net;

import com.geode.configurations.ProtocolConfigurations;

public class GeodeProtocol implements GeodeIdentifiable
{
    private final ProtocolConfigurations protocolConfigurations;

    public GeodeProtocol(ProtocolConfigurations protocolConfigurations)
    {
        this.protocolConfigurations = protocolConfigurations;
    }

    @Override
    public String getGeodeId()
    {
        return "GeodeProtocol::" + protocolConfigurations.getName() + "::" + protocolConfigurations.getProtocolClass().getName();
    }

    public ProtocolConfigurations getProtocolConfigurations()
    {
        return protocolConfigurations;
    }
}
