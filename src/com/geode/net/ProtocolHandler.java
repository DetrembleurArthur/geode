package com.geode.net;

import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnEvent;

import com.geode.annotations.Protocol;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class ProtocolHandler extends Thread implements Initializable
{
    private static final Logger logger = Logger.getLogger(ProtocolHandler.class);
    protected Object protocol;
    protected ArrayList<Method> controls;
    protected HashMap<String, QueryListener> dynamicConstrols;
    protected boolean running;
    protected TcpTunnel tunnel;
    protected GState gState;
    protected HashMap<OnEvent.Event, Method> listeners;
    protected int identifier;

    public ProtocolHandler(Socket socket)
    {
        dynamicConstrols = new HashMap<>();
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
            logger.fatal("handler error: " + e.getMessage());
        }
    }

    public synchronized void send(Query query)
    {
        try
        {
            tunnel.send(query);
        }
        catch(Exception e)
        {
            
        }
    }

    public Query recv()
    {
        Query query = null;
        try
        {
            query = tunnel.recv();
        }
        catch(Exception e)
        {

        }
        return query;
    }

    @Override
    public void init()
    {
        if(gState == GState.DOWN)
        {
            protocol = discovery();
            if(protocol == null) return;
            initControls();
            initInjections();
            initListeners();
            callListener(OnEvent.Event.INIT, new Object[0]);
            callListener(OnEvent.Event.INIT_OR_REBOOT, new Object[0]);
            logger.info("handler initialized");
            gState = GState.READY;
        }
        else
        {
            logger.fatal("handler " + gState + " can not be initialized");
        }
    }
    
    protected void callListener(OnEvent.Event event, Object[] args)
    {
    	Method method = listeners.getOrDefault(event, null);
    	if(method != null)
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
    	for(Method method : protocol.getClass().getDeclaredMethods())
        {
            if(method.isAnnotationPresent(OnEvent.class))
            {
            	OnEvent.Event event = method.getAnnotation(OnEvent.class).value();
                listeners.put(event, method);
                logger.info(method + " added has Listener : " + event);
            }
        }
    }
    
    private void initInjections()
    {
    	for(Field field : protocol.getClass().getDeclaredFields())
    	{
    		try
    		{
    			if(field.isAnnotationPresent(Inject.class))
        		{
        			if(field.getType().equals(ProtocolHandler.class)
					|| field.getType().equals(ClientProtocolHandler.class)
					|| field.getType().equals(ServerProtocolHandler.class))
        			{
        				field.set(protocol, this);
        			}
        			else
        			{
        				field.set(protocol, field.getType().getConstructor().newInstance());
        			}
        		}
    		}
    		catch(Exception e)
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

    protected boolean testControl(Control control)
    {
        return true;
    }

    private void initControls()
    {
        for(Method method : protocol.getClass().getDeclaredMethods())
        {
            if(method.isAnnotationPresent(Control.class))
            {
                if(testControl(method.getAnnotation(Control.class)))
                {
                    Control.Type type = method.getAnnotation(Control.class).type();
                    if(type == Control.Type.DIRECT || type == Control.Type.TOPIC)
                    {
                        if(method.getParameterTypes().length >= 1)
                        {
                            if(!method.getParameterTypes()[0].equals(Integer.class))
                            {
                                logger.warn(method + " DIRECT control must have an Integer as first parameter");
                                continue;
                            }
                        }
                    }
                    controls.add(method);
                    logger.info(method + " added has Control");
                }
                else
                {
                    logger.warn(method + " control ignored");
                }
            }
        }
    }

    protected abstract void end();

    @Override
    public void run()
    {
        init();
        if(gState == GState.READY)
        {
            logger.info("handler is running");
            running = true;
            while(running)
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
                }
                catch (Exception e)
                {
                    logger.error("unknown error: " + e.getMessage());
                }
                if(protocol.getClass().getAnnotation(Protocol.class).scope() == Protocol.Scope.QUERY)
                {
                    rebootProtocol();
                }
            }
            logger.info("handler is turning off");
            gState = GState.DOWN;
            callListener(OnEvent.Event.DOWN, new Object[0]);
        }
        else
        {
            logger.fatal("handler " + gState + " can not run");
        }
        end();
    }

    private void manageQueryResult(Query query, Object result) throws IOException
    {
        if(result != null)
        {
            if(result instanceof Query)
            {
                tunnel.send((Query)result);
            }
            else if(result instanceof String)
            {
                tunnel.send(Query.simple((String)result));
            }
            else if(result == Query.SUCCESS)
            {
                tunnel.send(Query.success(query.getType()));
            }
            else if(result == Query.FAILED)
            {
                tunnel.send(Query.failed(query.getType()));
            }
            else if(result instanceof Serializable)
            {
                tunnel.send(Query.simple(query.getType()).pack((Serializable)result));
            }
        }
    }

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
                logger.warn(query.getCategory() + " are not allowed here");
        }
        return null;
    }



    protected Object manageControlQuery(Query query, Control.Type ctype)
    {
        String type = query.getType();
        for(Method control : controls)
        {
            if(control.getAnnotation(Control.class).type() != ctype) continue;
        	String controlType = control.getAnnotation(Control.class).value().isEmpty() ?
        			control.getName() : control.getAnnotation(Control.class).value();
            if(type.equals(controlType))
            {
                Serializable[] args = query.getArgsArray();
                if(args.length == control.getParameterCount())
                {
                    int i = 0;
                    for(Class<?> paramClass : control.getParameterTypes())
                    {
                        if(!paramClass.isInstance(args[i]))
                        {
                            break;
                        }
                        i++;
                    }
                    if(i == args.length)
                    {
                        try
                        {
                            return control.invoke(protocol, args);
                        } catch (IllegalAccessException | InvocationTargetException e)
                        {
                            logger.error("error at control invokation: " + e.getMessage());
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
        QueryListener listener = dynamicConstrols.getOrDefault(type, null);
        if(listener != null)
            return listener.listen(query.getArgs());
        logger.error("no control found for " + query);
        return null;
    }

    protected abstract Object discovery();

    protected Object manageTopicSubscribeQuery(Query query)
    {
        logger.warn(query.getCategory() + " are not allowed here");
        return null;
    }

    protected Object manageTopicUnsubscribeQuery(Query query)
    {
        logger.warn(query.getCategory() + " are not allowed here");
        return null;
    }

    protected Object manageTopicNotifyQuery(Query query)
    {
        logger.warn(query.getCategory() + " are not allowed here");
        return null;
    }

    protected Object manageTopicNotifyOthersQuery(Query query)
    {
        logger.warn(query.getCategory() + " are not allowed here");
        return null;
    }

    protected Object manageNotifyQuery(Query query)
    {
        logger.warn(query.getCategory() + " are not allowed here");
        return null;
    }

    protected Object manageQueueSubscribeQuery(Query query)
    {
        logger.warn(query.getCategory() + " are not allowed here");
        return null;
    }

    protected Object manageQueueUnsubscribeQuery(Query query)
    {
        logger.warn(query.getCategory() + " are not allowed here");
        return null;
    }

    protected Object manageQueueConsumeQuery(Query query)
    {
        logger.warn(query.getCategory() + " are not allowed here");
        return null;
    }

    protected Object manageQueueProduceQuery(Query query)
    {
        logger.warn(query.getCategory() + " are not allowed here");
        return null;
    }

    public HashMap<String, QueryListener> getDynamicConstrols()
    {
        return dynamicConstrols;
    }

    public void setDynamicConstrols(HashMap<String, QueryListener> dynamicConstrols)
    {
        this.dynamicConstrols = dynamicConstrols;
    }

    public HashMap<OnEvent.Event, Method> getListeners()
    {
        return listeners;
    }

    public void setListeners(HashMap<OnEvent.Event, Method> listeners)
    {
        this.listeners = listeners;
    }

    public Object getProtocol()
	{
		return protocol;
	}

	public void setProtocol(Object protocol)
	{
		this.protocol = protocol;
	}

	public ArrayList<Method> getControls()
	{
		return controls;
	}

	public void setControls(ArrayList<Method> controls)
	{
		this.controls = controls;
	}

	public synchronized boolean isRunning()
	{
		return running;
	}

	public void setRunning(boolean running)
	{
		this.running = running;
	}

	public TcpTunnel getTunnel()
	{
		return tunnel;
	}

	public void setTunnel(TcpTunnel tcpTunnel)
	{
		this.tunnel = tcpTunnel;
	}

	public GState getgState()
	{
		return gState;
	}

	public void setgState(GState gState)
	{
		this.gState = gState;
	}

    public int getIdentifier()
    {
        return identifier;
    }
}
