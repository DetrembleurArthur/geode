package com.geode.net.info;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuildersMap
{
    private static final Logger logger = LogManager.getLogger(BuildersMap.class);
    private static HashMap<Class<?>, Class<Builder<?>>> buildersMap;
    
    static
    {
        buildersMap = new HashMap<>();
    }

    public static <T> void register(Class<?> objectClass, Class<T> builderClass)
    {
        buildersMap.put(objectClass, (Class<Builder<?>>) builderClass);
    }

    public static Class<Builder<?>> getBuilder(Class<?> objectClass)
    {
        return buildersMap.get(objectClass);
    }
}
