package com.geode.engine.binding;


public class Trigger
{
    private Property<?> dest;
    private Runnable runnable;

    public Property<?> getDest()
    {
        return dest;
    }

    public void setDest(Property<?> dest)
    {
        this.dest = dest;
    }

    public void trigger()
    {
        runnable.run();
    }

    public Runnable getRunnable()
    {
        return runnable;
    }

    public void setRunnable(Runnable runnable)
    {
        this.runnable = runnable;
    }
}
