package com.geode.net;

import com.geode.configurations.ClientConfigurations;
import com.geode.configurations.ProtocolConfigurations;
import com.geode.configurations.ServerConfigurations;
import com.geode.exceptions.GeodeException;
import com.geode.log.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GeodeClient implements Runnable, GeodeIdentifiable
{
    private final GeodeClientManager geodeClientManager;
    private final ClientConfigurations clientConfigurations;
    private final ArrayList<GeodeClientHandler> geodeClientHandlers;

    public GeodeClient(ClientConfigurations clientConfigurations, GeodeClientManager geodeClientManager) throws GeodeException
    {
        this.geodeClientManager = geodeClientManager;
        this.clientConfigurations = clientConfigurations;
        this.geodeClientHandlers = new ArrayList<>();
        run();
    }
    @Override
    public void run()
    {
        Log.out(this, "is launched");
        try
        {
            for(ProtocolConfigurations protocolConfigurations : clientConfigurations.getProtocolConfigurations())
            {
                Socket socket = new Socket(clientConfigurations.getServerIp(), clientConfigurations.getServerPort());
                try
                {
                    GeodeClientHandler geodeClientHandler = new GeodeClientHandler(socket, protocolConfigurations, this);
                    geodeClientHandlers.add(geodeClientHandler);
                } catch (GeodeException e)
                {
                    Log.err(this, e.getMessage());
                }
            }
        } catch (IOException e)
        {
            Log.err(this, e.getMessage());
        }
        Log.out(this, "end");
    }

    public GeodeClientManager getGeodeClientManager()
    {
        return geodeClientManager;
    }

    public ClientConfigurations getClientConfigurations()
    {
        return clientConfigurations;
    }

    public ArrayList<GeodeClientHandler> getGeodeClientHandlers()
    {
        return geodeClientHandlers;
    }

    @Override
    public String getGeodeId()
    {
        return "GeodeClient::" + clientConfigurations.getServerIp() + ":" + clientConfigurations.getServerPort();
    }
}
