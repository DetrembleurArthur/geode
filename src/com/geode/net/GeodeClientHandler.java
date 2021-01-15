package com.geode.net;

import com.geode.configurations.ProtocolConfigurations;
import com.geode.exceptions.GeodeException;

import java.net.Socket;
import java.util.ArrayList;

public class GeodeClientHandler extends GeodeHandler
{
    private GeodeClient geodeClient;

    public GeodeClientHandler(Socket socket, ProtocolConfigurations protocolConfigurations, GeodeClient geodeClient) throws GeodeException
    {
        super(socket);
        this.geodeClient = geodeClient;
        this.protocolConfigurations = protocolConfigurations;
        init();
    }

    @Override
    protected void discovery() throws GeodeException
    {
        Query query = recv();
        if(query.getType().equalsIgnoreCase("discovery"))
        {
            send(Query.simple(protocolConfigurations.getName()));
            query = recv();
            if(query.getType().equalsIgnoreCase("discovery_ok"))
            {
                try
                {
                    protocol = protocolConfigurations.getProtocolClass().newInstance();
                } catch (InstantiationException | IllegalAccessException e)
                {
                    throw new GeodeException(this, e.getMessage());
                }
                return;
            }
            throw new GeodeException(this, query.getArgs().get());
        }
        throw new GeodeException(this, protocolConfigurations.getName() + " could not be found in the protocol list");
    }

    public GeodeClient getGeodeClient()
    {
        return geodeClient;
    }
}
