package com.geode.net.tunnels;


import com.geode.net.queries.GeodeQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

/**
 * The type Tcp tunnel.
 */
@Deprecated
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

    }

    @Override
    public <T extends Serializable> T recv() throws IOException
    {

        return null;
    }

    public GeodeQuery recvQuery() throws IOException
    {
        return null;
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
