package com.geode.engine.entity.components.animation;

import org.joml.Vector2f;

public class Vector2fTimedAnimationBuilder extends TimedAnimationBuilder<Vector2f>
{
    public Vector2fTimedAnimationBuilder()
    {
        super(2);
    }

    @Override
    public TimedAnimationBuilder<Vector2f> from(Vector2f value)
    {
        actions[0].setStartValue(value.x);
        actions[1].setStartValue(value.y);
        return this;
    }

    @Override
    public TimedAnimationBuilder<Vector2f> to(Vector2f value)
    {
        actions[0].setEndValue(value.x);
        actions[1].setEndValue(value.y);
        return this;
    }
}
