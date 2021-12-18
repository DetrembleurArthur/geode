package com.geode.net;

import com.geode.annotations.Control;
import com.geode.annotations.Protocol;
import com.geode.logging.Logger;
import com.geode.net.GeodeQuery.Category;

import java.io.IOException;
import java.net.Socket;

/**
 * The type Client protocol handler.
 */
public class ClientProtocolHandler extends ProtocolHandler
{
    private static final Logger logger = new Logger(ClientProtocolHandler.class);
    private final Class<?> protocolClass;

    /**
     * Instantiates a new Client protocol handler.
     *
     * @param socket        the socket
     * @param protocolClass the protocol class
     */
    public ClientProtocolHandler(Socket socket, Class<?> protocolClass)
    {
        super(socket);
        this.protocolClass = protocolClass;
    }

    @Override
    protected Object discovery()
    {
        logger.info("start discovery protocol");
        try
        {
            GeodeQuery geodeQuery = tunnel.recv();
            if (geodeQuery.getType().equalsIgnoreCase("protocol"))
            {
                geodeQuery.setType("protocol_send");
                geodeQuery.pack(protocolClass.getAnnotation(Protocol.class).value());
                tunnel.send(geodeQuery);
                geodeQuery = tunnel.recv();
                if (geodeQuery.getType().equalsIgnoreCase("protocol_ok"))
                {
                    logger.info("protocol discovery success");
                    identifier = (int) geodeQuery.getArgs().get(0);
                    return protocolClass.getConstructor().newInstance();
                }
            }
        } catch (Exception e)
        {
            logger.fatal("protocol discovery failed: " + e.getMessage());
            gState = GState.BROKEN;
        }
        logger.fatal("protocol discovery failed");
        return null;
    }

    @Override
    protected Object manageTopicNotifyQuery(GeodeQuery geodeQuery)
    {
        return manageControlQuery(geodeQuery, Control.Type.TOPIC);
    }

    @Override
    protected Object manageTopicNotifyOthersQuery(GeodeQuery geodeQuery)
    {
        return manageTopicNotifyQuery(geodeQuery);
    }

    @Override
    protected Object manageNotifyQuery(GeodeQuery geodeQuery)
    {
        return manageControlQuery(geodeQuery, Control.Type.DIRECT);
    }

    @Override
    protected Object manageQueueConsumeQuery(GeodeQuery geodeQuery)
    {
        return manageControlQuery(geodeQuery, Control.Type.QUEUE);
    }

    /**
     * Subscribe topic.
     *
     * @param topic the topic
     */
    public void subscribeTopic(String topic)
    {
        send(new GeodeQuery(topic).setCategory(Category.TOPIC_SUBSCRIBE));
    }

    /**
     * Unsubscribe topic.
     *
     * @param topic the topic
     */
    public void unsubscribeTopic(String topic)
    {
        send(new GeodeQuery(topic).setCategory(Category.TOPIC_UNSUBSCRIBE));
    }

    /**
     * Subscribe queue.
     *
     * @param queue the queue
     */
    public void subscribeQueue(String queue)
    {
        send(new GeodeQuery(queue).setCategory(Category.QUEUE_SUBSCRIBE));
    }

    /**
     * Unsubscribe queue.
     *
     * @param queue the queue
     */
    public void unsubscribeQueue(String queue)
    {
        send(new GeodeQuery(queue).setCategory(Category.QUEUE_UNSUBSCRIBE));
    }

    @Override
    protected void end()
    {
        try
        {
            logger.info("closing socket...");
            tunnel.getSocket().close();
        } catch (IOException e)
        {
            e.printStackTrace();
            logger.warning("unable to close socket : " + e.getMessage());
        }
    }
}
