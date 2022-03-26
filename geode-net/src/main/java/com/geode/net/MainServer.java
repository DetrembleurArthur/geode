package com.geode.net;

public class MainServer
{
    public static void main(String[] args) throws Exception
    {
        Unconnection.internal(5000).sendto(new byte[]{10}, "127.0.0.1", 5000);

    }
}
