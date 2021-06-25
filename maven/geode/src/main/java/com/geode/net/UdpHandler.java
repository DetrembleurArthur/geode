package com.geode.net;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * The type Udp handler.
 */
public class UdpHandler extends Thread implements Initializable
{
    private UdpTunnel tunnel;
    private UdpInfos infos;
    private SimpleUdpListener defaultListener;
    private HashMap<String, SimpleUdpListener> listeners;

    /**
     * Instantiates a new Udp handler.
     *
     * @param udpInfos the udp infos
     */
    public UdpHandler(UdpInfos udpInfos)
    {
        this.infos = udpInfos;
        init();
        listeners = new HashMap<>();
    }

    @Override
    public void init()
    {
        if (infos.isBind())
        {
            try
            {
                tunnel = new UdpTunnel(new DatagramSocket(infos.getPort(), InetAddress.getByName(infos.getHost())));
            } catch (SocketException | UnknownHostException e)
            {
                e.printStackTrace();
            }
        } else
        {
            try
            {
                tunnel = new UdpTunnel(new DatagramSocket(), infos.getHost(), infos.getPort(), 4096);
                tunnel.setAutoConfDest(false);
            } catch (SocketException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Serializable obj = tunnel.recv();
                if (obj instanceof SimpleQuery)
                {
                    SimpleUdpListener listener = listeners.getOrDefault(((SimpleQuery) obj).getType(), null);
                    if (listener != null)
                        listener.listen(((SimpleQuery) obj).getArgs());
                } else
                {
                    if (defaultListener != null)
                        defaultListener.listen(obj);
                }
            } catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send.
     *
     * @param serializable the serializable
     */
    public void send(Serializable serializable)
    {
        try
        {
            System.out.println("send " + serializable);
            tunnel.send(serializable);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Recv t.
     *
     * @param <T> the type parameter
     * @return the t
     */
    public <T extends Serializable> T recv()
    {
        try
        {
            Serializable serializable = tunnel.recv();
            System.out.println("recv " + serializable);
            return (T) serializable;
        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets tunnel.
     *
     * @return the tunnel
     */
    public UdpTunnel getTunnel()
    {
        return tunnel;
    }

    /**
     * Sets tunnel.
     *
     * @param tunnel the tunnel
     */
    public void setTunnel(UdpTunnel tunnel)
    {
        this.tunnel = tunnel;
    }

    /**
     * Gets infos.
     *
     * @return the infos
     */
    public UdpInfos getInfos()
    {
        return infos;
    }

    /**
     * Sets infos.
     *
     * @param infos the infos
     */
    public void setInfos(UdpInfos infos)
    {
        this.infos = infos;
    }

    /**
     * Gets default listener.
     *
     * @return the default listener
     */
    public SimpleUdpListener getDefaultListener()
    {
        return defaultListener;
    }

    /**
     * Sets default listener.
     *
     * @param defaultListener the default listener
     */
    public void setDefaultListener(SimpleUdpListener defaultListener)
    {
        this.defaultListener = defaultListener;
    }

    /**
     * Gets listeners.
     *
     * @return the listeners
     */
    public HashMap<String, SimpleUdpListener> getListeners()
    {
        return listeners;
    }

    /**
     * Sets listeners.
     *
     * @param listeners the listeners
     */
    public void setListeners(HashMap<String, SimpleUdpListener> listeners)
    {
        this.listeners = listeners;
    }
}
