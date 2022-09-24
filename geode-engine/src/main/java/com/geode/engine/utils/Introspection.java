package com.geode.engine.utils;

import java.lang.reflect.Method;

public class Introspection
{
    public static Method getMethod(Class<?> _class, String methodName)
    {
        try
        {
            return _class.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
