package com.geode.net;

import com.geode.annotations.Control;
import com.geode.annotations.Protocol;
import com.geode.net.Query.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

/**
 * The type Client protocol handler.
 */
public class ClientProtocolHandler extends ProtocolHandler
{
    private static final Logger logger = LogManager.getLogger(ClientProtocolHandler.class);
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
        try
        {
            Query query = tunnel.recv();
            if (query.getType().equalsIgnoreCase("protocol"))
            {
                query.setType("protocol_send");
                query.pack(protocolClass.getAnnotation(Protocol.class).value());
                tunnel.send(query);
                query = tunnel.recv();
                if (query.getType().equalsIgnoreCase("protocol_ok"))
                {
                    logger.info("protocol discovery success");
                    identifier = (int) query.getArgs().get(0);
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
    protected Object manageTopicNotifyQuery(Query query)
    {
        return manageControlQuery(query, Control.Type.TOPIC);
    }

    @Override
    protected Object manageTopicNotifyOthersQuery(Query query)
    {
        return manageTopicNotifyQuery(query);
    }

    @Override
    protected Object manageNotifyQuery(Query query)
    {
        return manageControlQuery(query, Control.Type.DIRECT);
    }

    @Override
    protected Object manageQueueConsumeQuery(Query query)
    {
        return manageControlQuery(query, Control.Type.QUEUE);
    }

    /**
     * Subscribe topic.
     *
     * @param topic the topic
     */
    public void subscribeTopic(String topic)
    {
        send(new Query(topic).setCategory(Category.TOPIC_SUBSCRIBE));
    }

    /**
     * Unsubscribe topic.
     *
     * @param topic the topic
     */
    public void unsubscribeTopic(String topic)
    {
        send(new Query(topic).setCategory(Category.TOPIC_UNSUBSCRIBE));
    }

    /**
     * Subscribe queue.
     *
     * @param queue the queue
     */
    public void subscribeQueue(String queue)
    {
        send(new Query(queue).setCategory(Category.QUEUE_SUBSCRIBE));
    }

    /**
     * Unsubscribe queue.
     *
     * @param queue the queue
     */
    public void unsubscribeQueue(String queue)
    {
        send(new Query(queue).setCategory(Category.QUEUE_UNSUBSCRIBE));
    }

    @Override
    protected void end()
    {
        try
        {
            tunnel.getSocket().close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
