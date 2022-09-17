package com.geode.engine.entity.components.animation;

import com.geode.binding.NotifyProperty;
import com.geode.binding.Property;
import com.geode.engine.tweening.TFunc;
import com.geode.engine.tweening.TimedTweenAction;
import com.geode.engine.tweening.TweenFunction;
import lombok.Getter;

public class TimedAnimationBuilder
{
    @Getter
    private final TimedTweenAction action;

    public TimedAnimationBuilder()
    {
        action = new TimedTweenAction(0f, 0f, TFunc.LINEAR, null, 1000f, 1, false);
    }

    public TimedAnimationBuilder from(float value)
    {
        action.setStartValue(value);
        return this;
    }

    public TimedAnimationBuilder to(float value)
    {
        action.setEndValue(value);
        return this;
    }

    public TimedAnimationBuilder func(TweenFunction value)
    {
        action.setFunc(value);
        return this;
    }

    public TimedAnimationBuilder delay(float value)
    {
        action.setMaxDelay(value);
        return this;
    }

    public TimedAnimationBuilder cycle(int value)
    {
        action.setMaxCycle(value);
        return this;
    }

    public TimedAnimationBuilder back(boolean value)
    {
        action.setBack(value);
        return this;
    }

    public TimedAnimationBuilder property(NotifyProperty<Float> property)
    {
        action.setBinder(property::set);
        return this;
    }
}
