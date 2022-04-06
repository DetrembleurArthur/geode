package com.geode.net;

import com.geode.net.conf.ConfigurationLoader;
import com.geode.net.conf.info.GeodeInfo;

public class ConfMain
{
    public static void main(String[] args) throws Exception
    {
        ConfigurationLoader loader = ConfigurationLoader.get("geode/conf.yaml");
        GeodeInfo info = loader.load(GeodeInfo.class);
        System.out.println(info.getServerInfos().keySet());
    }
}
