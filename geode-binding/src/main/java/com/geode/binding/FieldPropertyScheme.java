package com.geode.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FieldPropertyScheme<T>
{
    private Method getter;
    private Method setter;

    public FieldPropertyScheme(Class<?> class_, String fieldName)
    {
            String field = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1).toLowerCase();
        try
        {
            getter = class_.getDeclaredMethod("get" + field);
            setter = class_.getDeclaredMethod("set" + field, getter.getReturnType());
        } catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }

    public NotifyProperty<T> create(Object obj)
    {
        NotifyProperty<T> property = null;
        try
        {
            property = new NotifyProperty<>((T) getter.invoke(obj));
            property.addCallback(value -> {
                try
                {
                    setter.invoke(obj, value);
                } catch (IllegalAccessException | InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            });
            return property;
        } catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return new NotifyProperty<>(null);
    }
}

