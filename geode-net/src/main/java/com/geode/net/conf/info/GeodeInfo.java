package com.geode.net.conf.info;

import com.geode.net.conf.Attribute;
import com.geode.net.conf.AttributeHolder;

import java.util.ArrayList;
import java.util.HashMap;

@AttributeHolder
public class GeodeInfo
{
    private HashMap<String, ServerInfo> serverInfos;

    public HashMap<String, ServerInfo> getServerInfos()
    {
        return serverInfos;
    }

    @Attribute(value = "servers", innerElement = ServerInfo.class)
    public void setServerInfos(HashMap<String, ServerInfo> serverInfos)
    {
        this.serverInfos = serverInfos;
    }
}
