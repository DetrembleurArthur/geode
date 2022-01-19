package com.geode.net.net;

import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

import com.geode.net.net.logging.Logger;
import com.geode.net.net.tls.TLSUtils;

public abstract class AbstractClient implements Initializable
{
    private static final Logger logger = new Logger(AbstractClient.class);
    protected final ClientInfos clientInfos;

    public AbstractClient(ClientInfos infos)
    {
        clientInfos = infos;
    }

    protected Socket initSocket() throws Exception
    {
        if(clientInfos.getTlsInfos().isTLSEnable())
        {
            logger.info("enabling TLS...", getClientInfos().getName());
            SSLSocketFactory factory = TLSUtils.getSocketFactory(clientInfos.getTlsInfos());
            return factory.createSocket(
                InetAddress.getByName(clientInfos.getHost()),
                clientInfos.getPort()
            );
        }
        return new Socket(getClientInfos().getHost(), getClientInfos().getPort());
    }
    
    public ClientInfos getClientInfos()
    {
        return clientInfos;
    }
}
