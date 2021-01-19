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
    private Object protocol;
    private ArrayList<Method> controls;
    private boolean running;
    private Tunnel tunnel;
    private GState gState;

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

    protected abstract Object discovery();

    @Override
    public void init()
    {
        if(gState == GState.DOWN)
        {
            protocol = discovery();
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
        if(gState == GState.READY)
        {
            logger.info("handler is running");
            running = true;
            while(running)
            {
                try
                {
                    Q query = tunnel.recv();
                    Serializable result = manageQuery(query);
                    manageQueryResult(result);
                } catch (IOException e)
                {
                    logger.error("receive IO error: " + e.getMessage());
                } catch (ClassNotFoundException e)
                {
                    logger.error("receive CLASS error: " + e.getMessage());
                }
            }
        }
        else
        {
            logger.fatal("handler " + gState + " can not run");
        }
    }

    private void manageQueryResult(Serializable result) throws IOException
    {
        if(result instanceof Q)
        {
            tunnel.send(result);
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
