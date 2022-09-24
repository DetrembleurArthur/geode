package com.geode.engine.timer;


import com.geode.engine.core.Time;

public class StaticTimer
{
    private float maxDelay;
    private float beginDelay = -1f;

    public StaticTimer(float maxDelay)
    {
        this.maxDelay = maxDelay;
    }

    public float getMaxDelay()
    {
        return maxDelay;
    }

    public void setMaxDelay(float maxDelay)
    {
        this.maxDelay = maxDelay;
    }

    public void activate()
    {
        beginDelay = Time.getTime() * 1000;
    }

    public void cancel()
    {
        beginDelay = -1f;
    }

    public boolean isRunning()
    {
        return beginDelay != -1f;
    }

    public boolean isFinished()
    {
        return (Time.getTime() * 1000 - beginDelay) >= maxDelay;
    }

    public float getElapsedTime()
    {
        return Time.getTime() * 1000 - beginDelay;
    }
}
