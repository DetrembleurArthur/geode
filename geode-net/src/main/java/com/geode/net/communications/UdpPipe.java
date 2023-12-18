package com.geode.net.communications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geode.net.connections.UdpConnection;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public abstract class UdpPipe implements Pipe
{
    protected final UdpConnection<?> connection;
    protected boolean resendMode;
    protected Object[] resendInfos = new Object[]{"host", 0};



    public UdpPipe(UdpConnection<?> connection, boolean resendMode)
    {
        this.connection = connection;
        this.resendMode = resendMode;
        System.out.println("create UDP pipe with resend mode at " + resendMode);
    }

    public void resend()
    {
        if(resendMode)
        {
            System.out.println("update resend infos");
            connection.setIp((String) resendInfos[0]);
            connection.setPort((Integer) resendInfos[1]);
        }
    }

    @Override
    public void send(Serializable data) throws IOException {
        connection.sendBytes((byte[]) data);
    }

    @Override
    public Serializable recv() throws Exception {
        byte[] bytes = connection.recvBytes(resendInfos);
        if(bytes != null)
        {
            resend();
            return bytes;
        }
        System.err.println("error at OBJECT reception");
        return null;
    }

    @Override
    public void close() throws IOException
    {
        connection.close();
        System.out.println("close UDP pipe");
    }



    public boolean isResendMode()
    {
        return resendMode;
    }

    public void setResendMode(boolean resendMode)
    {
        this.resendMode = resendMode;
    }

    @Override
    public boolean available() {
        return connection.getSocket().isConnected() || !connection.getSocket().isClosed();
    }
}
