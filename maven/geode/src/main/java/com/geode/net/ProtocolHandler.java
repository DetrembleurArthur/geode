package com.geode.net;

import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnEvent;
import com.geode.annotations.Protocol;
import com.geode.logging.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The type Protocol handler.
 */
public abstract class ProtocolHandler extends Thread implements Initializable
{
    private static final Logger logger = new Logger(ProtocolHandler.class);
    /**
     * The Protocol.
     */
    protected Object protocol;
    /**
     * The Controls.
     */
    protected ArrayList<Method> controls;
    /**
     * The Dynamic constrols.
     */
    protected HashMap<String, QueryListener> dynamicControls;
    /**
     * The Running.
     */
    protected boolean running;
    /**
     * The Tunnel.
     */
    protected TcpTunnel tunnel;
    /**
     * The G state.
     */
    protected GState gState;
    /**
     * The Listeners.
     */
    protected HashMap<OnEvent.Event, Method> listeners;
    /**
     * The Identifier.
     */
    protected int identifier;

    protected String protocolState = "DEFAULT";

    protected boolean enableDiscovery;

    /**
     * Instantiates a new Protocol handler.
     *
     * @param socket the socket
     */
    public ProtocolHandler(Socket socket, boolean discovery)
    {
        dynamicControls = new HashMap<>();
        controls = new ArrayList<>();
        listeners = new HashMap<>();
        running = false;
        gState = GState.DOWN;
        this.enableDiscovery = discovery;
        try
        {
            tunnel = new TcpTunnel(socket);
            logger.info("tunnel created");
        } catch (IOException e)
        {
            gState = GState.BROKEN;
            logger.fatal("handle error: " + e.getMessage());
        }
    }

    /**
     * Send.
     *
     * @param geodeQuery the query
     */
    public synchronized void send(GeodeQuery geodeQuery)
    {
        try
        {
            tunnel.send(geodeQuery);
        } catch (Exception e)
        {
            callListener(OnEvent.Event.SEND_ERROR, new Object[0]);
        }
    }

    /**
     * Recv query.
     *
     * @return the query
     */
    public GeodeQuery recv()
    {
        GeodeQuery geodeQuery = null;
        try
        {
            geodeQuery = tunnel.recv();
        } catch (Exception e)
        {
            callListener(OnEvent.Event.RECV_ERROR, new Object[0]);
        }
        return geodeQuery;
    }

    @Override
    public void init()
    {
        logger.info("initialisation");
        if (gState == GState.DOWN)
        {
            protocol = enableDiscovery ? discovery() : createProtocol();
            if (protocol == null) return;
            initControls();
            initInjections();
            initListeners();
            callListener(OnEvent.Event.INIT, new Object[0]);
            callListener(OnEvent.Event.INIT_OR_REBOOT, new Object[0]);
            logger.info("handler initialized");
            gState = GState.READY;
        } else
        {
            logger.fatal("handler " + gState + " can not be initialized");
        }
    }

    protected abstract Object createProtocol();

    /**
     * Call listener.
     *
     * @param event the event
     * @param args  the args
     */
    protected void callListener(OnEvent.Event event, Object[] args)
    {
        Method method = listeners.getOrDefault(event, null);
        if (method != null)
        {
            logger.info("invoke " + method + " % " + event);
            try
            {
                method.invoke(protocol, args);
            } catch (Exception e)
            {
                e.printStackTrace();
                logger.error("failed to invoke listener: " + method + " % " + event + " : " + e.getMessage());
            }
        }
    }

    private void initListeners()
    {
        logger.info("listeners initialisation");
        for (Method method : protocol.getClass().getDeclaredMethods())
        {
            if (method.isAnnotationPresent(OnEvent.class))
            {
                OnEvent.Event event = method.getAnnotation(OnEvent.class).value();
                listeners.put(event, method);
                logger.info(method + " added has Listener : " + event);
            }
        }
    }

    private void initInjections()
    {
        logger.info("injection initialisation");
        for (Field field : protocol.getClass().getDeclaredFields())
        {
            try
            {
                if (field.isAnnotationPresent(Inject.class))
                {
                    if (field.getType().equals(ProtocolHandler.class)
                            || field.getType().equals(ClientProtocolHandler.class)
                            || field.getType().equals(ServerProtocolHandler.class))
                    {
                        field.set(protocol, this);
                    } else
                    {
                        field.set(protocol, field.getType().getConstructor().newInstance());
                    }
                }
            } catch (Exception e)
            {
                logger.error("field injection failed: " + e.getMessage());
            }
        }
    }

    private void rebootProtocol()
    {
        try
        {
            protocol = protocol.getClass().getConstructor().newInstance();
            initInjections();
            logger.info("protocol " + protocol.getClass() + " reboot");
            callListener(OnEvent.Event.REBOOT, new Object[0]);
            callListener(OnEvent.Event.INIT_OR_REBOOT, new Object[0]);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Test control boolean.
     *
     * @param control the control
     * @return the boolean
     */
    protected boolean testControl(Control control)
    {
        return true;
    }

    private void initControls()
    {
        for (Method method : protocol.getClass().getDeclaredMethods())
        {
            if (method.isAnnotationPresent(Control.class))
            {
                if (testControl(method.getAnnotation(Control.class)))
                {
                    Control.Type type = method.getAnnotation(Control.class).type();
                    if (type == Control.Type.DIRECT || type == Control.Type.TOPIC)
                    {
                        if (method.getParameterTypes().length >= 1)
                        {
                            if (!method.getParameterTypes()[0].equals(Integer.class))
                            {
                                logger.warning(method + " DIRECT control must have an Integer as first parameter");
                                continue;
                            }
                        }
                    }
                    controls.add(method);
                    logger.info(method + " added has Control");
                } else
                {
                    logger.warning(method + " control ignored");
                }
            }
        }
    }

    /**
     * End.
     */
    public abstract void end();

    @Override
    public void run()
    {
        init();
        if (gState == GState.READY)
        {
            logger.info("handler is running");
            running = true;
            while (running)
            {
                try
                {
                    logger.info("wait for query...");
                    GeodeQuery geodeQuery = tunnel.recv();
                    callListener(OnEvent.Event.QUERY_IN, new Object[]{geodeQuery});
                    Object result = manageQuery(geodeQuery);
                    manageQueryResult(geodeQuery, result);
                } catch (IOException e)
                {
                    logger.error("receive IO error: " + e.getMessage());
                    running = false;
                } catch (ClassNotFoundException e)
                {
                    logger.error("receive CLASS error: " + e.getMessage());
                } catch (Exception e)
                {
                    logger.error("unknown error: " + e.getMessage());
                }
                if (protocol.getClass().getAnnotation(Protocol.class).scope() == Protocol.Scope.QUERY)
                {
                    rebootProtocol();
                }
            }
            logger.info("handler is turning off");
            gState = GState.DOWN;
            callListener(OnEvent.Event.DOWN, new Object[0]);
        } else
        {
            logger.fatal("handler " + gState + " can not run");
        }
        end();
    }

    private void manageQueryResult(GeodeQuery geodeQuery, Object result) throws IOException
    {
        if (result != null)
        {
            if (result instanceof GeodeQuery)
            {
                tunnel.send((GeodeQuery) result);
            } else if (result instanceof String)
            {
                tunnel.send(GeodeQuery.simple((String) result));
            } else if (result == GeodeQuery.SUCCESS)
            {
                tunnel.send(GeodeQuery.success(geodeQuery.getType()));
            } else if (result == GeodeQuery.FAILED)
            {
                tunnel.send(GeodeQuery.failed(geodeQuery.getType()));
            } else if (result instanceof Serializable)
            {
                tunnel.send(GeodeQuery.simple(geodeQuery.getType()).pack((Serializable) result));
            }
        }
    }

    /**
     * Manage query object.
     *
     * @param geodeQuery the query
     * @return the object
     */
    protected Object manageQuery(GeodeQuery geodeQuery)
    {
        switch (geodeQuery.getCategory())
        {
            case NORMAL:
                return manageControlQuery(geodeQuery, Control.Type.CLASSIC);
            case TOPIC_SUBSCRIBE:
                return manageTopicSubscribeQuery(geodeQuery);
            case TOPIC_UNSUBSCRIBE:
                return manageTopicUnsubscribeQuery(geodeQuery);
            case TOPIC_NOTIFY_OTHERS:
                return manageTopicNotifyOthersQuery(geodeQuery);
            case TOPIC_NOTIFY:
                return manageTopicNotifyQuery(geodeQuery);
            case NOTIFY:
                return manageNotifyQuery(geodeQuery);
            case QUEUE_SUBSCRIBE:
                return manageQueueSubscribeQuery(geodeQuery);
            case QUEUE_UNSUBSCRIBE:
                return manageQueueUnsubscribeQuery(geodeQuery);
            case QUEUE_CONSUME:
                return manageQueueConsumeQuery(geodeQuery);
            case QUEUE_PRODUCE:
                return manageQueueProduceQuery(geodeQuery);
            default:
                logger.warning(geodeQuery.getCategory() + " are not allowed here");
        }
        return null;
    }


    /**
     * Manage control query object.
     *
     * @param geodeQuery the query
     * @param ctype      the ctype
     * @return the object
     */
    protected Object manageControlQuery(GeodeQuery geodeQuery, Control.Type ctype)
    {
        String type = geodeQuery.getType();
        for (Method control : controls)
        {
            if (control.getAnnotation(Control.class).type() != ctype) continue;
            String controlType = control.getAnnotation(Control.class).value().isEmpty() ?
                    control.getName() : control.getAnnotation(Control.class).value();
            if (type.equals(controlType))
            {
                if (control.getAnnotation(Control.class).state().equals(protocolState))
                {
                    Serializable[] args = geodeQuery.getArgsArray();
                    if (args.length == control.getParameterCount())
                    {
                        int i = 0;
                        for (Class<?> paramClass : control.getParameterTypes())
                        {
                            if (!paramClass.isInstance(args[i]))
                            {
                                break;
                            }
                            i++;
                        }
                        if (i == args.length)
                        {
                            try
                            {
                                return control.invoke(protocol, args);
                            } catch (IllegalAccessException | InvocationTargetException e)
                            {
                                logger.error("error at control invocation: " + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        return manageDynamicControlQuery(geodeQuery);
    }

    private Object manageDynamicControlQuery(GeodeQuery geodeQuery)
    {
        String type = geodeQuery.getType();
        QueryListener listener = dynamicControls.getOrDefault(type, null);
        if (listener != null)
            return listener.listen(geodeQuery.getArgs());
        logger.error("no control found for " + geodeQuery);
        return null;
    }

    /**
     * Discovery object.
     *
     * @return the object
     */
    protected abstract Object discovery();

    /**
     * Manage topic subscribe query object.
     *
     * @param geodeQuery the query
     * @return the object
     */
    protected Object manageTopicSubscribeQuery(GeodeQuery geodeQuery)
    {
        logger.warning(geodeQuery.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage topic unsubscribe query object.
     *
     * @param geodeQuery the query
     * @return the object
     */
    protected Object manageTopicUnsubscribeQuery(GeodeQuery geodeQuery)
    {
        logger.warning(geodeQuery.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage topic notify query object.
     *
     * @param geodeQuery the query
     * @return the object
     */
    protected Object manageTopicNotifyQuery(GeodeQuery geodeQuery)
    {
        logger.warning(geodeQuery.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage topic notify others query object.
     *
     * @param geodeQuery the query
     * @return the object
     */
    protected Object manageTopicNotifyOthersQuery(GeodeQuery geodeQuery)
    {
        logger.warning(geodeQuery.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage notify query object.
     *
     * @param geodeQuery the query
     * @return the object
     */
    protected Object manageNotifyQuery(GeodeQuery geodeQuery)
    {
        logger.warning(geodeQuery.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage queue subscribe query object.
     *
     * @param geodeQuery the query
     * @return the object
     */
    protected Object manageQueueSubscribeQuery(GeodeQuery geodeQuery)
    {
        logger.warning(geodeQuery.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage queue unsubscribe query object.
     *
     * @param geodeQuery the query
     * @return the object
     */
    protected Object manageQueueUnsubscribeQuery(GeodeQuery geodeQuery)
    {
        logger.warning(geodeQuery.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage queue consume query object.
     *
     * @param geodeQuery the query
     * @return the object
     */
    protected Object manageQueueConsumeQuery(GeodeQuery geodeQuery)
    {
        logger.warning(geodeQuery.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage queue produce query object.
     *
     * @param geodeQuery the query
     * @return the object
     */
    protected Object manageQueueProduceQuery(GeodeQuery geodeQuery)
    {
        logger.warning(geodeQuery.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Gets dynamic constrols.
     *
     * @return the dynamic constrols
     */
    public HashMap<String, QueryListener> getDynamicControls()
    {
        return dynamicControls;
    }

    /**
     * Sets dynamic constrols.
     *
     * @param dynamicControls the dynamic constrols
     */
    public void setDynamicControls(HashMap<String, QueryListener> dynamicControls)
    {
        this.dynamicControls = dynamicControls;
    }

    /**
     * Gets listeners.
     *
     * @return the listeners
     */
    public HashMap<OnEvent.Event, Method> getListeners()
    {
        return listeners;
    }

    /**
     * Sets listeners.
     *
     * @param listeners the listeners
     */
    public void setListeners(HashMap<OnEvent.Event, Method> listeners)
    {
        this.listeners = listeners;
    }

    /**
     * Gets protocol.
     *
     * @return the protocol
     */
    public Object getProtocol()
    {
        return protocol;
    }

    /**
     * Sets protocol.
     *
     * @param protocol the protocol
     */
    public void setProtocol(Object protocol)
    {
        this.protocol = protocol;
    }

    /**
     * Gets controls.
     *
     * @return the controls
     */
    public ArrayList<Method> getControls()
    {
        return controls;
    }

    /**
     * Sets controls.
     *
     * @param controls the controls
     */
    public void setControls(ArrayList<Method> controls)
    {
        this.controls = controls;
    }

    /**
     * Is running boolean.
     *
     * @return the boolean
     */
    public synchronized boolean isRunning()
    {
        return running;
    }

    /**
     * Sets running.
     *
     * @param running the running
     */
    public void setRunning(boolean running)
    {
        this.running = running;
    }

    /**
     * Gets tunnel.
     *
     * @return the tunnel
     */
    public TcpTunnel getTunnel()
    {
        return tunnel;
    }

    /**
     * Sets tunnel.
     *
     * @param tcpTunnel the tcp tunnel
     */
    public void setTunnel(TcpTunnel tcpTunnel)
    {
        this.tunnel = tcpTunnel;
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

    /**
     * Gets identifier.
     *
     * @return the identifier
     */
    public int getIdentifier()
    {
        return identifier;
    }

    public String getProtocolState()
    {
        return protocolState;
    }

    public void setProtocolState(String protocolState)
    {
        this.protocolState = protocolState;
    }
}
