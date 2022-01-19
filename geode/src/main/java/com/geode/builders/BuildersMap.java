package com.geode.builders;

import java.util.HashMap;

import com.geode.logging.Logger;

public class BuildersMap
{
    private static final Logger logger = new Logger(BuildersMap.class);
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
