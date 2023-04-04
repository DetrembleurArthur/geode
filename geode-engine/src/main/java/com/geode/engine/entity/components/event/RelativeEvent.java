package com.geode.engine.entity.components.event;


public abstract class RelativeEvent extends Event
{
    protected Object object;

    public RelativeEvent(Object object)
    {
        this.object = object;
    }

    public Object getObject()
    {
        return object;
    }
}
