package com.geode.engine.entity.components.animation;


public class FloatTimedAnimationBuilder extends TimedAnimationBuilder<Float>
{
    public FloatTimedAnimationBuilder()
    {
        super(1);
    }

    @Override
    public TimedAnimationBuilder<Float> from(Float value)
    {
        actions[0].setStartValue(value);
        return this;
    }

    @Override
    public TimedAnimationBuilder<Float> to(Float value)
    {
        actions[0].setEndValue(value);
        return this;
    }
}
