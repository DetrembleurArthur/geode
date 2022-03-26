package com.geode.net;

import java.io.IOException;
import java.net.*;

public class Unconnection implements AutoCloseable
{
    protected DatagramSocket socket;

    public static Unconnection create() throws SocketException
    {
        Unconnection unconnection = new Unconnection();
        unconnection.setSocket(new DatagramSocket());
        return unconnection;
    }

    public static Unconnection on(String host, int port) throws UnknownHostException, SocketException
    {
        Unconnection unconnection = new Unconnection();
        DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName(host));
        unconnection.setSocket(socket);
        return unconnection;
    }

    public static Unconnection local(int port) throws UnknownHostException, SocketException
    {
        return on("0.0.0.0", port);
    }

    public static Unconnection internal(int port) throws UnknownHostException, SocketException
    {
        return on("127.0.0.1", port);
    }

    protected Unconnection()
    {

    }

    public DatagramPacket sendto(byte[] data, String ip, int port) throws IOException
    {
        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(ip), port);
        socket.send(packet);
        return packet;
    }

    public DatagramPacket sendto(byte[] data, InetAddress ip, int port) throws IOException
    {
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        socket.send(packet);
        return packet;
    }

    public DatagramPacket send(DatagramPacket packet) throws IOException
    {
        socket.send(packet);
        return packet;
    }

    public DatagramPacket recv(int size) throws IOException
    {
        byte[] buffer = new byte[size];
        DatagramPacket packet = new DatagramPacket(buffer, size);
        socket.receive(packet);
        return packet;
    }

    public DatagramSocket getSocket()
    {
        return socket;
    }

    public void setSocket(DatagramSocket socket)
    {
        this.socket = socket;
    }

    @Override
    public void close() throws Exception
    {
        socket.close();
    }
}
