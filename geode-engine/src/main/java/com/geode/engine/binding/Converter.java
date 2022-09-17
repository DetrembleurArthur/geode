package com.geode.engine.binding;

public interface Converter<T, U>
{
    U convert(T value);
}
