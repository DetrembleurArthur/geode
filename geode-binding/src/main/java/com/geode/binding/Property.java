package com.geode.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Property<T>
{
    private T value;

    public Property(T value)
    {
        this.value = value;
    }

    public void set(T value)
    {
        this.value = value;
    }

    public T get()
    {
        return value;
    }

    public static <U> Property<U> create(U value)
    {
        return new Property<>(value);
    }
}
