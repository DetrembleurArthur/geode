package com.geode.net;

import com.geode.configurations.ClientConfigurations;
import com.geode.configurations.ConfigurationBuilder;
import com.geode.configurations.ServerConfigurations;
import com.geode.exceptions.GeodeException;
import com.geode.xml.XmlNode;

import java.util.ArrayList;

public class GeodeClientManager extends GeodeManager implements GeodeIdentifiable
{
    private final ArrayList<GeodeClient> geodeClients;

    public GeodeClientManager(GeodeApplication geodeApplication)
    {
        super(geodeApplication);
        geodeClients = new ArrayList<>();
    }

    @Override
    public void initFromXmlNode(XmlNode node) throws GeodeException
    {
        for(XmlNode sub : node.getNodes())
        {
            ClientConfigurations clientConfigurations = null;
            try
            {
                clientConfigurations = ConfigurationBuilder.buildClientConfiguration(sub);
            } catch (Exception e)
            {
                throw new GeodeException(this, e.getMessage());
            }
            GeodeClient geodeClient = new GeodeClient(clientConfigurations, this);
            geodeClients.add(geodeClient);
        }
    }

    public ArrayList<GeodeClient> getGeodeClients()
    {
        return geodeClients;
    }

    @Override
    public String getGeodeId()
    {
        return "GeodeClientManager";
    }
}
