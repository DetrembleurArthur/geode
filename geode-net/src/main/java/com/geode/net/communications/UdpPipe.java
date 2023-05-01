package com.geode.net.communications;

import com.geode.net.connections.UdpConnection;

import java.io.IOException;

public abstract class UdpPipe<T> implements Pipe<T>
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
}
