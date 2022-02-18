package com.geode.net.protocols;

import com.geode.net.ProtocolHandler;
import com.geode.net.annotations.Control;
import com.geode.net.annotations.Inject;
import com.geode.net.annotations.OnEvent;
import com.geode.net.annotations.Protocol;
import com.geode.net.queries.GeodeQuery;

import java.net.Inet4Address;
import java.net.UnknownHostException;

@Protocol("scenario")
public class ClientScenarioProtocol
{
    @Inject
    public ProtocolHandler handler;

    @OnEvent
    public void init()
    {
        handler.send(GeodeQuery.simple("hostname"));
    }

    @Control()
    public GeodeQuery server_hostname(String serverHostname) throws UnknownHostException
    {
        System.out.println("Server hostname: " + serverHostname);
        return GeodeQuery.simple("HOSTNAME-SCENARIO").pack(Inet4Address.getLocalHost().getHostName());
    }
}
