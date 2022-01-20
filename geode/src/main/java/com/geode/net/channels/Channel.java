package com.geode.net.channels;

public class Channel<T>
{
    private T sharedValue;
    private boolean waitAvailable = true;

    public Channel()
    {

    }

    public Channel(T initialValue)
    {
        sharedValue = initialValue;
    }

    public synchronized T get()
    {
        return sharedValue;
    }

    public synchronized void set(T sharedValue)
    {
        this.sharedValue = sharedValue;
    }

    public synchronized T waitValue()
    {
        while (waitAvailable)
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
                return null;
            }
        }
        waitAvailable = true;
        return sharedValue;
    }

    public synchronized void notifyValue(T sharedValue)
    {
        this.sharedValue = sharedValue;
        waitAvailable = false;
        notify();
    }
}
