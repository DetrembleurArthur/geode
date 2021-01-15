package com.geode.net;

import com.geode.log.Log;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;

public class Tunnel
{
    private final Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public Tunnel(Socket socket)
    {
        try
        {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e)
        {
            Log.err("Tunnel", "unable to init object streams");
        }
        this.socket = socket;
    }

    public Socket getSocket()
    {
        return socket;
    }

    public void sendobj(Serializable serializable)
    {
        try
        {
            objectOutputStream.writeObject(serializable);
            objectOutputStream.flush();
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.err("Tunnel", "unable to send [" + serializable + "]");
        }
    }

    public <T extends Serializable> T recvobj()
    {
        try
        {
            return (T) objectInputStream.readObject();
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.err("Tunnel", "unable to recieve an object");
        }
        return null;
    }

    public ObjectOutputStream getObjectOutputStream()
    {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream()
    {
        return objectInputStream;
    }
}
