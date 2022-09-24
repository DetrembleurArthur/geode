package com.geode.engine.entity.components.event;

import com.geode.engine.entity.GameObject;

public class CollisionEvent extends RelativeEvent
{
    private GameObject object;

    public CollisionEvent(GameObject parent, GameObject object)
    {
        super(parent);
        this.object = object;
    }

    @Override
    boolean isAppend()
    {
        return ((GameObject)getObject()).collision_c().contains(object);
    }
}
