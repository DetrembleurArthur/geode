package com.geode.net.protocols;

import com.geode.net.annotations.Control;
import com.geode.net.annotations.Protocol;
import com.geode.net.queries.GeodeQuery;

import java.net.Inet4Address;
import java.net.UnknownHostException;

@Protocol("scenario")
public class ServerScenarioProtocol
{
    @Control()
    public GeodeQuery hostname() throws UnknownHostException
    {
        return GeodeQuery.simple("server_hostname").pack(Inet4Address.getLocalHost().getHostName());
    }

    @Control()
    public void client_hostname(String clientHostname)
    {
        System.out.println("Client hostname: " + clientHostname);
    }
}
