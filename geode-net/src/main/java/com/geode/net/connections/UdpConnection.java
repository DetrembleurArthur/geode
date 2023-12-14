package com.geode.net.connections;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class UdpConnection<T extends DatagramSocket> implements Closeable, Connection
{
    protected final T socket;
    protected String ip;
    protected int port;

    protected UdpConnection(T socket)
    {
        this(socket, "127.0.0.1", 0);
    }

    protected UdpConnection(T socket, String ip, int port)
    {
        this.socket = socket;
        this.ip = ip;
        this.port = port;
        System.out.println("create UDP connection: " + socket + " with sender " + ip + ":" + port);
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
        System.out.println("change sender ip to " + ip);
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
        System.out.println("change sender port to " + port);
    }

    public DatagramPacket sendBytes(byte[] data) throws IOException
    {
        System.out.println("prepare meta packet of size " + data.length);
        DatagramPacket packet = prepareMetaPacketToSend(data.length);
        System.out.println("send meta packet: " + packet);
        socket.send(packet);
        packet = preparePacketToSend(data);
        socket.send(packet);
        System.out.println("prepare and send packet: " + packet);
        return packet;
    }

    public byte[] recvBytes(Object[] resendInfos) throws IOException
    {
        DatagramPacket packet = prepareMetaPacketToRecv();
        System.out.println("wait meta packet");
        socket.receive(packet);
        System.out.println("receive meta packet: " + packet);
        var buff = ByteBuffer.wrap(packet.getData());
        var type = buff.get();
        if(type == 0)
        {
            var len  = buff.getInt();
            packet = preparePacketToRecv(len);
            System.out.println("wait packet");
            socket.receive(packet);
            System.out.println("receive packet: " + packet);
            buff = ByteBuffer.wrap(packet.getData());
            if(buff.get() == 1)
            {
                resendInfos[0] = packet.getAddress().getHostAddress();
                resendInfos[1] = packet.getPort();
                byte[] dest = new byte[packet.getData().length - 1];
                buff.get(dest);
                return dest;
            }
            else
            {
                System.err.println("UDP packet must be of type 1 not " + type);
            }
        }
        else
        {
            System.err.println("UDP packet must be of type 0 not " + type);
        }
        return null;
    }

    public DatagramPacket prepareMetaPacketToSend(int size) throws UnknownHostException
    {
        byte[] bytes = ByteBuffer.allocate(5).put((byte)0).putInt(size).array();
        return new DatagramPacket(bytes, bytes.length, InetAddress.getByName(ip), port);
    }

    public DatagramPacket prepareMetaPacketToRecv()
    {
        return preparePacketToRecv(Integer.BYTES + 1);
    }

    public DatagramPacket preparePacketToSend(byte[] data) throws UnknownHostException
    {
        data = ByteBuffer.allocate(data.length + 1).put((byte) 1).put(data).array();
        return new DatagramPacket(data, data.length, InetAddress.getByName(ip), port);
    }

    public DatagramPacket preparePacketToRecv(int recvSize)
    {
        byte[] buffer = new byte[recvSize + 1];
        return new DatagramPacket(buffer, buffer.length);
    }

    @Override
    public void close()
    {
        socket.close();
        System.out.println("close UDP connection: " + socket);
    }

    public DatagramSocket getSocket()
    {
        return socket;
    }
}
