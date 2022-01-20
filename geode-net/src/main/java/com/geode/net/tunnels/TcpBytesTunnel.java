package com.geode.net.tunnels;


import com.geode.net.misc.LowLevelSerializer;
import com.geode.net.queries.GeodeQuery;
import com.geode.net.queries.LowQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The type Tcp tunnel.
 */
public class TcpBytesTunnel extends Tunnel<Socket>
{
    private static final Logger logger = LogManager.getLogger(TcpBytesTunnel.class);
    private final DataOutputStream dataOutputStream;
    private final DataInputStream dataInputStream;

    /**
     * Instantiates a new Tcp tunnel.
     *
     * @param socket the socket
     * @throws IOException the io exception
     */
    public TcpBytesTunnel(Socket socket) throws IOException
    {
        super(socket);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());
        logger.info("tunnel initialized on " + socket);
    }

    @Override
    public void send(Serializable serializable) throws IOException
    {
        logger.info("send " + serializable);
        try
        {
            byte[] bytes = LowLevelSerializer.serialize(serializable);
            logger.debug(bytes.length);
            dataOutputStream.write(bytes);
        } catch (InvocationTargetException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        dataOutputStream.flush();
    }

    @Override
    public <T extends Serializable> T recv() throws IOException
    {
        int size = dataInputStream.readInt();
        logger.debug(size);
        byte[] obj = dataInputStream.readNBytes(size);
        logger.info("recv " + obj);
        try
        {
            return (T) LowLevelSerializer.deserialize(obj);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public GeodeQuery recvQuery() throws IOException
    {
        ArrayList<Object> objects = recv();
        System.out.println(objects.size());
        return ((LowQuery)objects.get(0)).toGeodeQuery();
    }

    public DataOutputStream getDataOutputStream()
    {
        return dataOutputStream;
    }

    public DataInputStream getDataInputStream()
    {
        return dataInputStream;
    }
}
