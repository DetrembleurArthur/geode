package com.geode.net.net;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The type Udp tunnel.
 */
public class UdpTunnel extends Tunnel<DatagramSocket>
{
    private InetAddress to;
    private int dport;
    private final int packetSize;
    private boolean autoConfDest;

    /**
     * Instantiates a new Udp tunnel.
     *
     * @param socket     the socket
     * @param to         the to
     * @param dport      the dport
     * @param packetSize the packet size
     */
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

    /**
     * Instantiates a new Udp tunnel.
     *
     * @param socket the socket
     */
    public UdpTunnel(DatagramSocket socket)
    {
        this(socket, "127.0.0.1", 50000, 4096);
    }

    /**
     * Serialize byte [ ].
     *
     * @param obj the obj
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public static byte[] serialize(Object obj) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        os.flush();
        return out.toByteArray();
    }

    /**
     * Deserialize object.
     *
     * @param data the data
     * @return the object
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
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

    /**
     * Gets to.
     *
     * @return the to
     */
    public InetAddress getTo()
    {
        return to;
    }

    /**
     * Sets to.
     *
     * @param to the to
     */
    public void setTo(InetAddress to)
    {
        this.to = to;
    }

    /**
     * Sets to.
     *
     * @param to the to
     */
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

    /**
     * Gets dport.
     *
     * @return the dport
     */
    public int getDport()
    {
        return dport;
    }

    /**
     * Sets dport.
     *
     * @param dport the dport
     */
    public void setDport(int dport)
    {
        this.dport = dport;
    }

    /**
     * Is auto conf dest boolean.
     *
     * @return the boolean
     */
    public boolean isAutoConfDest()
    {
        return autoConfDest;
    }

    /**
     * Sets auto conf dest.
     *
     * @param autoConfDest the auto conf dest
     */
    public void setAutoConfDest(boolean autoConfDest)
    {
        this.autoConfDest = autoConfDest;
    }
}
