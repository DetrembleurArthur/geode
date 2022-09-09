package com.geode.binding;

public interface Converter<T, U>
{
    U convert(T value);
}
