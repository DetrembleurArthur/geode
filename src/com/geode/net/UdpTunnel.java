package com.geode.net;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UdpTunnel extends Tunnel<DatagramSocket>
{
    private InetAddress to;
    private int dport;
    private int packetSize;
    private boolean autoConfDest;

    public UdpTunnel(DatagramSocket socket, String to, int dport, int packetSize)
    {
        super(socket);
        try
        {
            this.to = InetAddress.getByName(to);
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        this.dport = dport;
        autoConfDest = true;
        this.packetSize = packetSize;
    }

    public UdpTunnel(DatagramSocket socket)
    {
        this(socket, "127.0.0.1", 50000, 4096);
    }

    @Override
    public void send(Serializable serializable) throws IOException
    {
        byte[] serializedObject = serialize(serializable);
        DatagramPacket packet = new DatagramPacket(serializedObject, serializedObject.length, to, dport);
        socket.send(packet);
    }

    @Override
    public <T extends Serializable> T recv() throws IOException, ClassNotFoundException
    {
        byte[] bytes = new byte[packetSize];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        to = packet.getAddress();
        dport = packet.getPort();
        return (T) deserialize(packet.getData());
    }

    public static byte[] serialize(Object obj) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        os.flush();
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public InetAddress getTo()
    {
        return to;
    }

    public void setTo(InetAddress to)
    {
        this.to = to;
    }

    public void setTo(String to)
    {
        try
        {
            this.to = InetAddress.getByName(to);
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
    }

    public int getDport()
    {
        return dport;
    }

    public void setDport(int dport)
    {
        this.dport = dport;
    }

    public boolean isAutoConfDest()
    {
        return autoConfDest;
    }

    public void setAutoConfDest(boolean autoConfDest)
    {
        this.autoConfDest = autoConfDest;
    }
}
