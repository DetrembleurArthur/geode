package com.geode.configurations;

import java.util.ArrayList;

public class ServerConfigurations implements Configurations
{
    private String ip = "0.0.0.0";
    private int port = 4001;
    private final ArrayList<ProtocolConfigurations> protocolConfigurations;

    public ServerConfigurations()
    {
        this.protocolConfigurations = new ArrayList<>();
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public ArrayList<ProtocolConfigurations> getProtocolConfigurations()
    {
        return protocolConfigurations;
    }
}
