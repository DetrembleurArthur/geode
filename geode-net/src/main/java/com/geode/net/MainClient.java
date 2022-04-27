package com.geode.net;

import com.geode.net.access.Connection;
import com.geode.net.mgmt.ConnectionHandler;

public class MainClient
{
    public static void main(String[] args) throws Exception
    {
        Connection connection = Connection.internal(50000);
        ConnectionHandler handler = new ConnectionHandler(connection);
        handler.runAsThread();
        handler.obj().send("Hello :)");
    }
}
