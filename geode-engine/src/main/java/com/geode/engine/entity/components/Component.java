package com.geode.engine.entity.components;

import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.Updatable;
import lombok.Getter;

public abstract class Component implements Updatable
{
    @Getter
    private final GameObject parent;

    public Component(GameObject parent)
    {
        this.parent = parent;
    }
}
