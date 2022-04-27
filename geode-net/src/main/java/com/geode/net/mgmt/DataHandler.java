package com.geode.net.mgmt;

import java.io.Serializable;

public abstract class DataHandler<T extends Serializable, U>
{
    private final U context;

    protected DataHandler(U context)
    {
        this.context = context;
    }

    public final void manage(T object)
    {
        handleResult(handle(object));
    }

    public abstract Object handle(T object);
    public abstract void handleResult(Object result);

    public U getContext()
    {
        return context;
    }
}
