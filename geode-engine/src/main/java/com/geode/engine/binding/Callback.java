package com.geode.engine.binding;

public interface Callback<T>
{
    void call(T value);
}
