package com.geode.engine.entity.components;

import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.Updatable;
import lombok.Getter;

public abstract class Component implements Updatable
{
    public static final int DEFAULT_PRIORITY = 0;
    public static final int MIN_PRIORITY = Integer.MIN_VALUE;
    public static final int MAX_PRIORITY = Integer.MAX_VALUE;

    @Getter
    private final GameObject parent;

    @Getter
    private final int priority;

    public Component(GameObject parent)
    {
        this(parent, DEFAULT_PRIORITY);
    }

    public Component(GameObject parent, int priority)
    {
        this.parent = parent;
        this.priority = priority;
    }
}
