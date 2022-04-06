package com.geode.net.mgmt;

import com.geode.net.access.Connection;

public interface ConnectionHandler extends AutoCloseable
{
    void handleConnection(Connection connection);
}
