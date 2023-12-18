package com.geode.net.communications;

import com.geode.crypto.Serializer;
import com.geode.net.connections.UdpConnection;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;

public class UdpObjectPipe extends UdpPipe
{
    public UdpObjectPipe(UdpConnection<?> gateway, boolean serverMode)
    {
        super(gateway, serverMode);
        System.out.println("create UDP OBJECT pipe");
    }

    @Override
    public void send(Serializable data) throws IOException
    {
        super.send(Serializer.serialize(data));
        System.out.println("send OBJECT: " + data);
    }

    @Override
    public Serializable recv() throws Exception {
        System.out.println("wait OBJECT");
        byte[] bytes = (byte[]) super.recv();
        Serializable serializable = (Serializable) Serializer.deserialize(bytes);
        System.out.println("receive OBJECT: " + serializable);
        return serializable;
    }
}
