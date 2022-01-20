package com.geode.net;

import com.geode.net.info.ClientInfos;
import com.geode.net.tls.TLSUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLSocketFactory;
import java.net.InetAddress;
import java.net.Socket;

public abstract class AbstractClient implements Initializable
{
    private static final Logger logger = LogManager.getLogger(AbstractClient.class);
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
