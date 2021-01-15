package com.geode.net;

import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnInit;
import com.geode.configurations.ProtocolConfigurations;
import com.geode.exceptions.GeodeException;
import com.geode.log.Log;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class GeodeHandler implements GeodeIdentifiable, Runnable
{
    protected final Tunnel tunnel;
    protected Object protocol;
    protected final Thread thread;
    protected boolean running = false;
    protected final HashMap<String, Method> controls;
    protected ProtocolConfigurations protocolConfigurations;

    public GeodeHandler(Socket socket) throws GeodeException
    {
        controls = new HashMap<>();
        tunnel = new Tunnel(socket);
        thread = new Thread(this);
    }

    protected void init() throws GeodeException
    {
        discovery();
        initControls();
        injections();
        callOnInitMethods();
        thread.start();
    }

    private void callOnInitMethods()
    {
        for(Method method : protocol.getClass().getMethods())
        {
            try
            {
                if (method.isAnnotationPresent(OnInit.class))
                {
                    method.invoke(protocol);
                }
            }
            catch (Exception e)
            {
                Log.err(this, e.getMessage());
            }
        }
    }

    private void injections() throws GeodeException
    {
        for(Field field : protocol.getClass().getDeclaredFields())
        {
            if(field.isAnnotationPresent(Inject.class))
            {
                if(field.getType().equals(GeodeHandler.class))
                {
                    try
                    {
                        field.set(protocol, this);
                    } catch (IllegalAccessException e)
                    {
                        throw new GeodeException(this, e.getMessage());
                    }
                }
                else
                {
                    try
                    {
                        field.set(protocol, field.getType().newInstance());
                    } catch (IllegalAccessException | InstantiationException e)
                    {
                        throw new GeodeException(this, e.getMessage());
                    }
                }
            }
        }
    }

    abstract protected void discovery() throws GeodeException;

    private void initControls()
    {
        for(Method method : protocol.getClass().getDeclaredMethods())
        {
            if(method.isAnnotationPresent(Control.class))
            {
                Control control = method.getAnnotation(Control.class);
                String type = control.type().isEmpty() ? method.getName() : control.type();
                controls.put(type + "#" + method.getParameterCount(), method);
            }
        }
    }

    @Override
    public void run()
    {
        running = true;
        while(running)
        {
            Log.out(this, "wait query");
            Query query = recv();
            try
            {
                Object result = handleQuery(query);
                handleResult(query, result);
            } catch (Exception e)
            {
                Log.err(this, e.getMessage());
            }
        }
        Log.out(this, "end");
    }

    private void handleResult(Query oldQuery, Object result)
    {
        if(result != null)
        {
            if(result instanceof Query)
            {
                send((Query)result);
            }
            else if(result instanceof String)
            {
                send(Query.simple((String) result));
            }
            else
            {
                if(result == Query.SUCCESS)
                    send(Query.success(oldQuery.getType()));
                else if(result == Query.FAILED)
                    send(Query.failed(oldQuery.getType()));
                else
                    send(Query.simple(oldQuery.getType()).pack((Serializable) result));
            }
        }
    }

    private Object handleQuery(Query query) throws Exception
    {
        switch (query.getMode())
        {
            case NORMAL:
                return handleNormalQuery(query);
        }
        return null;
    }

    private Object handleNormalQuery(Query query) throws Exception
    {
        String type = query.getType();
        if(controls.containsKey(type + "#" + query.getArgs().size()))
        {
            return controls.get(type + "#" + query.getArgs().size()).invoke(protocol, query.getArgs().toArray());
        }
        else
        {
            Log.err(this, query.getType() + " is not a recognized control");
        }
        return null;
    }

    public void send(Query query)
    {
        Log.out(this, "Send [" + query + "]");
        tunnel.sendobj(query);
    }

    public Query recv()
    {
        Query query = tunnel.recvobj();
        Log.out(this, "Recv [" + query + "]");
        return query;
    }

    public Tunnel getTunnel()
    {
        return tunnel;
    }

    public Object getProtocol()
    {
        return protocol;
    }

    public Thread getThread()
    {
        return thread;
    }

    public boolean isRunning()
    {
        return running;
    }

    @Override
    public String getGeodeId()
    {
        if(protocolConfigurations != null)
            return "GeodeHandler::" + protocolConfigurations.getName() + "::" + tunnel.getSocket().getInetAddress().getHostAddress() + ":" + tunnel.getSocket().getLocalPort();
        return "GeodeHandler::" + tunnel.getSocket().getInetAddress().getHostAddress() + ":" + tunnel.getSocket().getLocalPort();
    }
}
