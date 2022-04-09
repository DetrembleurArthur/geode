package com.geode.net;

import com.geode.net.access.ConnectionListener;
import com.geode.net.mgmt.Server;

public class MainServer
{
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(ConnectionListener.internal(50000, 5));
        server.runAsThread();
    }
}
