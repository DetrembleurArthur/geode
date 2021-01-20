package com.geode.net;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Geode
{
    private CopyOnWriteArrayList<Server> servers;
    private CopyOnWriteArrayList<Client> clients;
    
    public Geode()
    {
    	servers = new CopyOnWriteArrayList<>();
    	clients = new CopyOnWriteArrayList<>();
    }
    
    public Geode buildServer(Server server)
    {
    	servers.add(server);
    	return this;
    }
    
    public Geode buildClient(Client client)
    {
    	clients.add(client);
    	return this;
    }
}
