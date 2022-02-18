package com.geode.net;

import com.geode.net.filters.Filter;
import com.geode.net.info.ClientInfos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

/**
 * The type Client.
 */
public class Client extends AbstractClient implements Runnable
{
    private static final Logger logger = LogManager.getLogger(Client.class);

    /**
     * The Handler.
     */
    protected ClientProtocolHandler handler;
    /**
     * The G state.
     */
    protected GState gState;

    /**
     * Instantiates a new Client.
     *
     * @param clientInfos the client infos
     */
    public Client(ClientInfos clientInfos)
    {
        super(clientInfos);
        gState = GState.DOWN;
    }

    @Override
    public void init()
    {
        logger.info("initialisation", getClientInfos().getName());
        try
        {
            Socket socket = initSocket();
            logger.info("client connected : " + socket, getClientInfos().getName());
            handler = new ClientProtocolHandler(socket, clientInfos.getProtocolClass(),
                clientInfos.isEnableDiscovery(), clientInfos.getChannelsManager(),
                    clientInfos.getChannelsManagerInfos(), clientInfos.getCommunicationMode());
                handler.getFilters().add(Filter.createCategoryFilter(clientInfos.getFiltersInfos().getQueryCategories()));
                handler.setBundleFilter(clientInfos.getFiltersInfos().getBundle());
            handler.enableChecksumFilter(clientInfos.getFiltersInfos().isChecksum());
                gState = GState.READY;
        } catch (Exception e)
        {
            logger.fatal("client connection error: " + e.getMessage(), getClientInfos().getName());
            gState = GState.BROKEN;
        }
    }

    @Override
    public void run()
    {
        logger.info("pre init");
        init();
        logger.info("let's running");
        logger.info("async activated");
        if (gState == GState.READY)
        {
            gState = GState.RUNNING;
            handler.start();
            logger.info("client handler is running", getClientInfos().getName());
        } else
        {
            logger.fatal("client " + gState + " can not run", getClientInfos().getName());
        }
    }

    /**
     * Gets protocol class.
     *
     * @return the protocol class
     */
    public Class<?> getProtocolClass()
    {
        return clientInfos.getProtocolClass();
    }

    /**
     * Gets handler.
     *
     * @return the handler
     */
    public ClientProtocolHandler getHandler()
    {
        return handler;
    }

    /**
     * Sets handler.
     *
     * @param handler the handler
     */
    public void setHandler(ClientProtocolHandler handler)
    {
        this.handler = handler;
    }

    /**
     * Gets handler safe.
     *
     * @return the handler safe
     */
    public ClientProtocolHandler getHandlerSafe()
    {
        while (!handler.isRunning()) ;
        return handler;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public GState getgState()
    {
        return gState;
    }

    /**
     * Sets state.
     *
     * @param gState the g state
     */
    public void setgState(GState gState)
    {
        this.gState = gState;
    }
}
