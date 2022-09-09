package com.geode.binding;

public interface Callback<T>
{
    void call(T value);
}
