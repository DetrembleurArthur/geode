package com.geode.builders;


public abstract class Builder<T>
{
    protected T object;

    public Builder()
    {

    }

    public abstract Builder<T> reset();

    public T build()
    {
        return object;
    }

    public void clean()
    {
        object = null;
    }
}
