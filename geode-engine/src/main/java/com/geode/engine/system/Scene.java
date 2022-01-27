package com.geode.engine.system;

import lombok.Getter;
import lombok.Setter;

public abstract class Scene<T extends Application> implements Manageable
{
    @Getter @Setter
    private boolean keepState = false;

    @Getter @Setter
    private T parent;

    @Getter
    private boolean loaded = false;

    public void disactive()
    {
        if(!keepState)
        {
            destroy();
        }
    }

    public void active()
    {
        if(!loaded)
        {
            load();
            loaded = true;
        }
        else
        {
            if(!keepState)
            {
                load();
            }
        }
    }

    public void asCurrent()
    {
        getParent().setScene(this);
    }
}
