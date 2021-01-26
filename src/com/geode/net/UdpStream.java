package com.geode.net;

import java.io.*;
import java.net.*;

public class UdpStream
{
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private boolean client;

    public UdpStream(String ip, int port, boolean client)
    {
        this.client = client;
        try
        {
            if(!client)
            {
                socket = new DatagramSocket(port, InetAddress.getByName(ip));

            }else
            {
                address = InetAddress.getByName(ip);
                this.port = port;
                socket = new DatagramSocket();
            }
        } catch (SocketException | UnknownHostException e)
        {
            e.printStackTrace();
        }
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

    public void send(Serializable serializable)
    {
        try
        {
            byte[] bytes = serialize(serializable);
            DatagramPacket datagramPacket;
            if(!client)
                datagramPacket =  new DatagramPacket(bytes, bytes.length);
            else
                datagramPacket = new DatagramPacket(bytes, bytes.length, address, port);
            socket.send(datagramPacket);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public <T extends Serializable> T recv(int maxSize)
    {
        byte[] bytes = new byte[maxSize];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        try
        {
            socket.receive(packet);
            return (T) deserialize(packet.getData());
        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public <T extends Serializable> T recv()
    {
        return recv(1028);
    }
}
