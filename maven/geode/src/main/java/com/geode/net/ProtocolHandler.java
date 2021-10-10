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

    /**
     * Instantiates a new Protocol handler.
     *
     * @param socket the socket
     */
    public ProtocolHandler(Socket socket)
    {
        dynamicControls = new HashMap<>();
        controls = new ArrayList<>();
        listeners = new HashMap<>();
        running = false;
        gState = GState.DOWN;
        try
        {
            tunnel = new TcpTunnel(socket);
        } catch (IOException e)
        {
            gState = GState.BROKEN;
            logger.fatal("handle error: " + e.getMessage());
        }
    }

    /**
     * Send.
     *
     * @param query the query
     */
    public synchronized void send(Query query)
    {
        try
        {
            tunnel.send(query);
        } catch (Exception e)
        {

        }
    }

    /**
     * Recv query.
     *
     * @return the query
     */
    public Query recv()
    {
        Query query = null;
        try
        {
            query = tunnel.recv();
        } catch (Exception e)
        {

        }
        return query;
    }

    @Override
    public void init()
    {
        logger.info("initialisation");
        if (gState == GState.DOWN)
        {
            protocol = discovery();
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
    protected abstract void end();

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
                    Query query = tunnel.recv();
                    callListener(OnEvent.Event.QUERY_IN, new Object[]{query});
                    Object result = manageQuery(query);
                    manageQueryResult(query, result);
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

    private void manageQueryResult(Query query, Object result) throws IOException
    {
        if (result != null)
        {
            if (result instanceof Query)
            {
                tunnel.send((Query) result);
            } else if (result instanceof String)
            {
                tunnel.send(Query.simple((String) result));
            } else if (result == Query.SUCCESS)
            {
                tunnel.send(Query.success(query.getType()));
            } else if (result == Query.FAILED)
            {
                tunnel.send(Query.failed(query.getType()));
            } else if (result instanceof Serializable)
            {
                tunnel.send(Query.simple(query.getType()).pack((Serializable) result));
            }
        }
    }

    /**
     * Manage query object.
     *
     * @param query the query
     * @return the object
     */
    protected Object manageQuery(Query query)
    {
        switch (query.getCategory())
        {
            case NORMAL:
                return manageControlQuery(query, Control.Type.CLASSIC);
            case TOPIC_SUBSCRIBE:
                return manageTopicSubscribeQuery(query);
            case TOPIC_UNSUBSCRIBE:
                return manageTopicUnsubscribeQuery(query);
            case TOPIC_NOTIFY_OTHERS:
                return manageTopicNotifyOthersQuery(query);
            case TOPIC_NOTIFY:
                return manageTopicNotifyQuery(query);
            case NOTIFY:
                return manageNotifyQuery(query);
            case QUEUE_SUBSCRIBE:
                return manageQueueSubscribeQuery(query);
            case QUEUE_UNSUBSCRIBE:
                return manageQueueUnsubscribeQuery(query);
            case QUEUE_CONSUME:
                return manageQueueConsumeQuery(query);
            case QUEUE_PRODUCE:
                return manageQueueProduceQuery(query);
            default:
                logger.warning(query.getCategory() + " are not allowed here");
        }
        return null;
    }


    /**
     * Manage control query object.
     *
     * @param query the query
     * @param ctype the ctype
     * @return the object
     */
    protected Object manageControlQuery(Query query, Control.Type ctype)
    {
        String type = query.getType();
        for (Method control : controls)
        {
            if (control.getAnnotation(Control.class).type() != ctype) continue;
            String controlType = control.getAnnotation(Control.class).value().isEmpty() ?
                    control.getName() : control.getAnnotation(Control.class).value();
            if (type.equals(controlType))
            {
                if(control.getAnnotation(Control.class).state().equals(protocolState))
                {
                    Serializable[] args = query.getArgsArray();
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
        return manageDynamicControlQuery(query);
    }

    private Object manageDynamicControlQuery(Query query)
    {
        String type = query.getType();
        QueryListener listener = dynamicControls.getOrDefault(type, null);
        if (listener != null)
            return listener.listen(query.getArgs());
        logger.error("no control found for " + query);
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
     * @param query the query
     * @return the object
     */
    protected Object manageTopicSubscribeQuery(Query query)
    {
        logger.warning(query.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage topic unsubscribe query object.
     *
     * @param query the query
     * @return the object
     */
    protected Object manageTopicUnsubscribeQuery(Query query)
    {
        logger.warning(query.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage topic notify query object.
     *
     * @param query the query
     * @return the object
     */
    protected Object manageTopicNotifyQuery(Query query)
    {
        logger.warning(query.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage topic notify others query object.
     *
     * @param query the query
     * @return the object
     */
    protected Object manageTopicNotifyOthersQuery(Query query)
    {
        logger.warning(query.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage notify query object.
     *
     * @param query the query
     * @return the object
     */
    protected Object manageNotifyQuery(Query query)
    {
        logger.warning(query.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage queue subscribe query object.
     *
     * @param query the query
     * @return the object
     */
    protected Object manageQueueSubscribeQuery(Query query)
    {
        logger.warning(query.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage queue unsubscribe query object.
     *
     * @param query the query
     * @return the object
     */
    protected Object manageQueueUnsubscribeQuery(Query query)
    {
        logger.warning(query.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage queue consume query object.
     *
     * @param query the query
     * @return the object
     */
    protected Object manageQueueConsumeQuery(Query query)
    {
        logger.warning(query.getCategory() + " are not allowed here");
        return null;
    }

    /**
     * Manage queue produce query object.
     *
     * @param query the query
     * @return the object
     */
    protected Object manageQueueProduceQuery(Query query)
    {
        logger.warning(query.getCategory() + " are not allowed here");
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
