package com.geode.net;

import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnEvent;

import org.apache.log4j.Logger;

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
    protected boolean running;
    protected Tunnel tunnel;
    protected GState gState;
    protected HashMap<OnEvent.Event, Method> listeners;
    protected int identifier;

    public ProtocolHandler(Socket socket)
    {
        controls = new ArrayList<>();
        listeners = new HashMap<OnEvent.Event, Method>();
        running = false;
        gState = GState.DOWN;
        try
        {
            tunnel = new Tunnel(socket);
        } catch (IOException e)
        {
            gState = GState.BROKEN;
            logger.fatal("handler error: " + e.getMessage());
        }
    }

    public synchronized void send(Q query)
    {
        try
        {
            tunnel.send(query);
        }
        catch(Exception e)
        {
            
        }
    }

    public Q recv()
    {
        Q query = null;
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
                    Q query = tunnel.recv();
                    Serializable result = manageQuery(query);
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
            }
            logger.info("handler is turning off");
            gState = GState.DOWN;
        }
        else
        {
            logger.fatal("handler " + gState + " can not run");
        }
    }

    private void manageQueryResult(Q query, Serializable result) throws IOException
    {
        if(result != null)
        {
            if(result instanceof Q)
            {
                tunnel.send(result);
            }
            else if(result instanceof String)
            {
                tunnel.send(Q.simple((String)result));
            }
            else if(result == Q.SUCCESS)
            {
                tunnel.send(Q.success(query.getType()));
            }
            else if(result == Q.FAILED)
            {
                tunnel.send(Q.failed(query.getType()));
            }
            else
            {
                tunnel.send(Q.simple(query.getType()).pack(result));
            }
        }
    }

    protected Serializable manageQuery(Q query)
    {
        switch (query.getCategory())
        {
            case NORMAL:
                return manageControlQuery(query, Control.Type.SERVER_CLIENT);
            case TOPIC_SUBSCRIBE:
                return (Serializable) manageTopicSubscribeQuery(query);
            case TOPIC_UNSUBSCRIBE:
                return (Serializable) manageTopicUnsubscribeQuery(query);
            case TOPIC_NOTIFY_OTHERS:
                return (Serializable) manageTopicNotifyOthersQuery(query);
            case TOPIC_NOTIFY:
                return (Serializable) manageTopicNotifyQuery(query);
            default:
                logger.warn(query.getCategory() + " are not allowed here");
        }
        return null;
    }



    protected Serializable manageControlQuery(Q query, Control.Type ctype)
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
                            return (Serializable) control.invoke(protocol, args);
                        } catch (IllegalAccessException | InvocationTargetException e)
                        {
                            logger.error("error at control invokation: " + e.getMessage());
                        }
                    }
                }
            }
        }
        logger.error("no control found for " + query);
        return null;
    }

    protected abstract Object discovery();

    protected Object manageTopicSubscribeQuery(Q query)
    {
        logger.warn(query.getCategory() + " are not allowed here");
        return null;
    }

    protected Object manageTopicUnsubscribeQuery(Q query)
    {
        logger.warn(query.getCategory() + " are not allowed here");
        return null;
    }

    protected Object manageTopicNotifyQuery(Q query)
    {
        logger.warn(query.getCategory() + " are not allowed here");
        return null;
    }

    protected Object manageTopicNotifyOthersQuery(Q query)
    {
        logger.warn(query.getCategory() + " are not allowed here");
        return null;
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

	public Tunnel getTunnel()
	{
		return tunnel;
	}

	public void setTunnel(Tunnel tunnel)
	{
		this.tunnel = tunnel;
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
