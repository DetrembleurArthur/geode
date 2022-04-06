package com.geode.net;

import com.geode.net.access.Connection;
import com.geode.net.access.ConnectionListener;
import com.geode.net.access.Unconnection;
import com.geode.net.mgmt.ConnectionHandler;
import com.geode.net.mgmt.Server;

public class MainServer
{
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(
                ConnectionListener.internal(50000, 5),
                new ConnectionHandler()
                {
                    @Override
                    public void handleConnection(Connection connection)
                    {
                        System.out.println("New connection: " + connection.getSocket().getInetAddress());
                    }

                    @Override
                    public void close() throws Exception
                    {

                    }
                });
        server.run();

    }
}
