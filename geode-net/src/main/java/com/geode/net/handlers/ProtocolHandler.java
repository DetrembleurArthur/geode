package com.geode.net.handlers;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import com.geode.net.communications.Pipe;
import com.geode.net.protocols.Protocol;
import com.geode.net.protocols.Target;
import com.geode.net.query.Query;

public class ProtocolHandler implements Runnable {
    
    private Pipe<Serializable> pipe;
    private ArrayList<Class<?>> protocols;
    private HashMap<String, HashMap<String, ArrayList<Method>>> protocolsMap = new HashMap<>();
    private HashMap<String, Object> protocolInstancesMap = new HashMap<>();
    private HashMap<String, ArrayList<Method>> currentProtocolMap;
    private Object currentProtocol;

    public ProtocolHandler(Pipe<Serializable> pipe, ArrayList<Class<?>> protocols) throws Exception
    {
        this.pipe = pipe;
        this.protocols = protocols;
        initProtocolsTriggers();
    }

    private void initProtocolsTriggers() throws Exception
    {
        for(Class<?> protocolClass : protocols)
        {
            System.out.println("check " + protocolClass.getSimpleName() + " class");
            if(protocolClass.isAnnotationPresent(Protocol.class))
            {
                HashMap<String, ArrayList<Method>> triggersMap = new HashMap<>();
                Object protocolInstance = protocolClass.getConstructor().newInstance();
                if(currentProtocolMap == null)
                {
                    currentProtocolMap = triggersMap;
                    currentProtocol = protocolInstance;
                }
                String protocolName = protocolClass.getAnnotation(Protocol.class).value();
                protocolsMap.put(protocolName, triggersMap);
                protocolInstancesMap.put(protocolName, protocolInstance);
                System.out.println("register " + protocolName + " protocol");
                for(Method method : protocolClass.getDeclaredMethods())
                {
                    if(method.isAnnotationPresent(Target.class))
                    {
                        String targetName = method.getAnnotation(Target.class).value();
                        if(!triggersMap.containsKey(targetName))
                            triggersMap.put(targetName, new ArrayList<>());
                        triggersMap.get(targetName).add(method);
                        System.out.println("register " + protocolName +":" + targetName + " target");
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        int errorStreakCounter = 0;
        while(pipe.available())
        {
            try {
                Query query = (Query) pipe.recv();
                String type = query.getType();
                ArrayList<Serializable> data = query.getData();
                ArrayList<Method> methods = currentProtocolMap.get(type);
                boolean find = true;
                for(Method method : methods)
                {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if(parameterTypes.length == data.size())
                    {
                        for(int i = 0; i < parameterTypes.length; i++)
                        {
                            if(data.get(i).getClass() != parameterTypes[i])
                            {
                                find = false;
                                break;
                            }
                        }
                        if(find)
                        {
                            Object result = method.invoke(currentProtocol, data.toArray());
                            if(result != null)
                            {
                                if(result instanceof Query) {
                                    pipe.send((Query) result);
                                } else if(result instanceof String) {
                                    pipe.send(Query.Simple(query.getType() + "." + result));
                                } else if(result instanceof Serializable) {
                                    pipe.send(query.renew().add((Serializable) result));
                                }
                            }
                            break;
                        }
                    }
                }
                errorStreakCounter = 0;
            } catch (SocketException e) {
                e.printStackTrace();
                System.err.println("socket error");
                break;
            } catch (Exception e) {
                errorStreakCounter++;
                e.printStackTrace();
                if(errorStreakCounter > 10)
                {
                    System.err.println("error streak counter max limit");
                    break;
                }
            }
        }
    }
}
