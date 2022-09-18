package com.geode.engine.entity.components.animation;


import com.geode.engine.binding.NotifyProperty;
import com.geode.engine.tweening.TFunc;
import com.geode.engine.tweening.TimedTweenAction;
import com.geode.engine.tweening.TweenFunction;
import lombok.Getter;

public abstract class TimedAnimationBuilder<T>
{
    @Getter
    protected TimedTweenAction[] actions = null;

    public TimedAnimationBuilder(int n)
    {
        actions = new TimedTweenAction[n];
        for(int i = 0; i < n; i++)
            actions[i] = new TimedTweenAction(0f, 0f, TFunc.LINEAR, null, 1000f, 1, false);
    }

    public abstract TimedAnimationBuilder<T> from(T value);

    public abstract TimedAnimationBuilder<T> to(T value);

    public TimedAnimationBuilder<T> func(TweenFunction value)
    {
        for(TimedTweenAction action : actions)
            action.setFunc(value);
        return this;
    }

    public TimedAnimationBuilder<T> delay(float value)
    {
        for(TimedTweenAction action : actions)
            action.setMaxDelay(value);
        return this;
    }

    public TimedAnimationBuilder<T> cycle(int value)
    {
        for(TimedTweenAction action : actions)
            action.setMaxCycle(value);
        return this;
    }

    public TimedAnimationBuilder<T> back(boolean value)
    {
        for(TimedTweenAction action : actions)
            action.setBack(value);
        return this;
    }

    public TimedAnimationBuilder<T> property(NotifyProperty<Float>[] properties)
    {
        for(int i = 0; i < actions.length; i++)
            actions[i].setBinder(properties[i]::set);
        return this;
    }

    public TimedAnimationBuilder<T> whenFinished(Runnable runnable)
    {
        actions[0].setWhenFinished(runnable);
        return this;
    }

    public void start()
    {
        for(TimedTweenAction action : actions)
            action.start();
    }
}
