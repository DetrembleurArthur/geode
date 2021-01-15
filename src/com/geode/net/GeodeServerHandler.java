package com.geode.net;

import com.geode.annotations.Control;
import com.geode.configurations.ProtocolConfigurations;
import com.geode.exceptions.GeodeException;
import com.geode.log.Log;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class GeodeServerHandler extends GeodeHandler
{
    private GeodeServer geodeServer;

    public GeodeServerHandler(Socket socket, GeodeServer geodeServer) throws GeodeException
    {
        super(socket);
        this.geodeServer = geodeServer;
        init();
    }

    @Override
    protected void discovery() throws GeodeException
    {
        ArrayList<ProtocolConfigurations> protocolConfigurations = geodeServer.getServerConfigurations().getProtocolConfigurations();
        send(new Query("discovery"));
        Query query = recv();
        String protocolName = query.getType();
        for(ProtocolConfigurations configuration : protocolConfigurations)
        {
            if(configuration.getName().equalsIgnoreCase(protocolName))
            {
                try
                {
                    protocol = configuration.getProtocolClass().newInstance();
                    this.protocolConfigurations = configuration;
                    send(Query.simple("discovery_ok"));
                    return;
                } catch (InstantiationException | IllegalAccessException e)
                {
                    send(new Query("discovery_error").pack("protocol " + protocolName + " is not able to be instantiate (" + e.getMessage() + ")"));
                    throw new GeodeException(this, e.getMessage());
                }
            }
        }
        send(new Query("discovery_error").pack(protocolName + " could not be found in the protocol list"));
        throw new GeodeException(this, protocolName + " could not be found in the protocol list");
    }

    public GeodeServer getGeodeServer()
    {
        return geodeServer;
    }
}
