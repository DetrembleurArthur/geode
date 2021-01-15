package com.geode.net;

import com.geode.configurations.ConfigurationBuilder;
import com.geode.configurations.ServerConfigurations;
import com.geode.exceptions.GeodeException;
import com.geode.xml.XmlNode;

import java.io.IOException;
import java.util.ArrayList;

public class GeodeServerManager extends GeodeManager implements GeodeIdentifiable
{
    private final ArrayList<GeodeServer> geodeServers;

    public GeodeServerManager(GeodeApplication geodeApplication)
    {
        super(geodeApplication);
        geodeServers = new ArrayList<>();
    }

    public ArrayList<GeodeServer> getGeodeServers()
    {
        return geodeServers;
    }

    @Override
    public void initFromXmlNode(XmlNode node) throws GeodeException
    {
        for(XmlNode sub : node.getNodes())
        {
            ServerConfigurations serverConfigurations = null;
            try
            {
                serverConfigurations = ConfigurationBuilder.buildServerConfiguration(sub);
            } catch (Exception e)
            {
                throw new GeodeException(this, e.getMessage());
            }
            GeodeServer geodeServer = new GeodeServer(serverConfigurations, this);
            geodeServers.add(geodeServer);
        }
    }

    @Override
    public String getGeodeId()
    {
        return "GeodeServerManager";
    }
}
