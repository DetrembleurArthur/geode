package com.geode.net.communications;

import com.geode.crypto.Serializer;
import com.geode.net.connections.UdpConnection;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;

public class UdpObjectPipe extends UdpPipe<Serializable>
{
    public UdpObjectPipe(UdpConnection<?> gateway, boolean serverMode)
    {
        super(gateway, serverMode);
        System.out.println("create UDP OBJECT pipe");
    }

    @Override
    public void send(Serializable data) throws IOException
    {
        byte[] dataBytes = Serializer.serialize(data);
        connection.sendBytes(dataBytes);
        System.out.println("send OBJECT: " + data);
    }

    @Override
    public Serializable recv() throws IOException, ParseException, ClassNotFoundException
    {
        System.out.println("wait OBJECT");
        byte[] bytes = connection.recvBytes(resendInfos);
        if(bytes != null)
        {
            resend();
            Serializable serializable = (Serializable) Serializer.deserialize(bytes);
            System.out.println("receive OBJECT: " + serializable);
            return serializable;
        }
        System.err.println("error at OBJECT reception");
        return new Serializable()
        {
            @Override
            public int hashCode()
            {
                return super.hashCode();
            }
        };
    }
}
