package com.geode.engine.entity.components.animation;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Vector4fTimedAnimationBuilder extends TimedAnimationBuilder<Vector4f>
{
    public Vector4fTimedAnimationBuilder()
    {
        super(4);
    }

    @Override
    public TimedAnimationBuilder<Vector4f> from(Vector4f value)
    {
        actions[0].setStartValue(value.x);
        actions[1].setStartValue(value.y);
        actions[2].setStartValue(value.z);
        actions[3].setStartValue(value.w);
        return this;
    }

    @Override
    public TimedAnimationBuilder<Vector4f> to(Vector4f value)
    {
        actions[0].setEndValue(value.x);
        actions[1].setEndValue(value.y);
        actions[2].setEndValue(value.z);
        actions[3].setEndValue(value.w);
        return this;
    }
}
