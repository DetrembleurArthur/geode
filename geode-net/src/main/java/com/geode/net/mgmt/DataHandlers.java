package com.geode.net.mgmt;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class DataHandlers
{
    public static HashMap<String, Class<? extends DataHandler<?, ?>>> dataHandlerMap = new HashMap<>();

    public static <T extends DataHandler<?, ?>> void register(Class<T> handlerClass)
    {
        DHandler handler = handlerClass.getAnnotation(DHandler.class);
        if (handler != null)
        {
            dataHandlerMap.put(handler.value(), handlerClass);
        }
    }

    public static DataHandler<?, ?> get(String id, Object context)
    {
        try
        {
            return dataHandlerMap.get(id).getConstructor(context.getClass()).newInstance(context);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
