package com.geode.net.connections;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastGroupConnection extends UdpConnection<MulticastSocket>
{
    protected MulticastGroupConnection(MulticastSocket socket)
    {
        super(socket);
        System.out.println("as Multicast group connection");
    }

    protected MulticastGroupConnection(MulticastSocket socket, String ip, int port)
    {
        super(socket, ip, port);
    }

    public static MulticastGroupConnection emitter(String groupIp, int port) throws IOException
    {
        MulticastSocket socket = new MulticastSocket();
        MulticastGroupConnection groupConnection = new MulticastGroupConnection(socket);
        groupConnection.setIp(groupIp);
        groupConnection.setPort(port);
        return groupConnection;
    }

    public static MulticastGroupConnection receiver(String groupIp, int port) throws IOException
    {
        MulticastSocket socket = new MulticastSocket(port);
        MulticastGroupConnection groupConnection = new MulticastGroupConnection(socket);
        groupConnection.setIp(groupIp);
        groupConnection.setPort(port);
        return groupConnection;
    }

    public void join() throws IOException
    {
        socket.joinGroup(InetAddress.getByName(ip));
        System.out.println("Join " + ip + " group");
    }

    public void leave() throws IOException
    {
        socket.leaveGroup(InetAddress.getByName(ip));
        System.out.println("Leave " + ip + " group");
    }
}
