package com.geode.net;

import com.geode.annotations.Control;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;

public abstract class ProtocolHandler extends Thread implements Initializable
{
    private static final Logger logger = Logger.getLogger(ProtocolHandler.class);
    protected Object protocol;
    protected ArrayList<Method> controls;
    protected boolean running;
    protected Tunnel tunnel;
    protected GState gState;

    public ProtocolHandler(Socket socket)
    {
        controls = new ArrayList<>();
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

    public void send(Q query)
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

    protected abstract Object discovery();

    @Override
    public void init()
    {
        if(gState == GState.DOWN)
        {
            protocol = discovery();
            if(protocol == null) return;
            initControls();
            logger.info("handler initialized");
            gState = GState.READY;
        }
        else
        {
            logger.fatal("handler " + gState + " can not be initialized");
        }
    }

    private void initControls()
    {
        for(Method method : protocol.getClass().getDeclaredMethods())
        {
            if(method.isAnnotationPresent(Control.class))
            {
                controls.add(method);
                logger.info(method + " added has Control");
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

    private Serializable manageQuery(Q query)
    {
        switch (query.getCategory())
        {
            case NORMAL:
                return manageNormalQuery(query);
            default:
                logger.warn(query.getCategory() + " are not allowed here");
        }
        return null;
    }

    private Serializable manageNormalQuery(Q query)
    {
        String type = query.getType();
        for(Method control : controls)
        {
            if(type.equals(control.getName()))
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
}
