package com.geode.engine.entity;

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
