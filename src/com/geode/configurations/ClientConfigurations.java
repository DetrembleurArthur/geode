package com.geode.configurations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClientConfigurations implements Configurations
{
    private String serverIp;
    private int serverPort;
    private final ArrayList<ProtocolConfigurations> protocolConfigurations;

    public ClientConfigurations()
    {
        this.protocolConfigurations = new ArrayList<>();
    }

    public String getServerIp()
    {
        return serverIp;
    }

    public void setServerIp(String serverIp)
    {
        this.serverIp = serverIp;
    }

    public int getServerPort()
    {
        return serverPort;
    }

    public void setServerPort(int serverPort)
    {
        this.serverPort = serverPort;
    }

    public ArrayList<ProtocolConfigurations> getProtocolConfigurations()
    {
        return protocolConfigurations;
    }
}
