package com.geode.net.connections;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpSimpleConnection extends UdpConnection<DatagramSocket>
{
    private UdpSimpleConnection(DatagramSocket socket)
    {
        super(socket);
    }

    public UdpSimpleConnection(DatagramSocket socket, String ip, int port)
    {
        super(socket, ip, port);
        System.out.println("as simple UDP connection");
    }

    public static UdpSimpleConnection client(String ip, int port) throws SocketException
    {
        DatagramSocket socket = new DatagramSocket();
        return new UdpSimpleConnection(socket, ip, port);
    }

    public static UdpSimpleConnection on(String host, int port) throws UnknownHostException, SocketException
    {
        DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName(host));
        return new UdpSimpleConnection(socket);
    }

    public static UdpSimpleConnection local(int port) throws SocketException, UnknownHostException
    {
        return on("0.0.0.0", port);
    }

    public static UdpSimpleConnection internal(int port) throws UnknownHostException, SocketException
    {
        return on("127.0.0.1", port);
    }
}
