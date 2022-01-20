package com.geode.net.queries;

import java.io.Serializable;

import static com.geode.net.queries.GeodeQuery.Category.FORWARD;

public class ForwardQuery extends GeodeQuery
{
    static public class Forwarder implements Serializable
    {
        public String ip;
        public int port;
        public boolean waitResponse;
    }

    private Forwarder forwarder;

    @Override
    public GeodeQuery pack(Serializable... args)
    {
        return args[0] instanceof GeodeQuery ? super.pack(args[0]) : null;
    }

    public GeodeQuery forwarder(String ip, int port, boolean wr)
    {
        this.forwarder = new Forwarder();
        this.forwarder.ip = ip;
        this.forwarder.port = port;
        this.forwarder.waitResponse = wr;
        return this;
    }

    public Forwarder getForwarder()
    {
        return forwarder;
    }

    public String toString()
    {
        return "FW " + forwarder.ip + ":" + forwarder.port + " [" + type + "::" + FORWARD + "::" + args + "]";
    }
}
