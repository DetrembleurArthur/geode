package com.geode.engine.core;

import com.geode.engine.graphics.Camera;
import lombok.Getter;
import lombok.Setter;

public abstract class Scene<T extends Application> implements Manageable
{
    @Getter
    private Camera camera;

    @Getter @Setter
    private boolean keepState = false;

    @Getter @Setter
    private T parent;

    @Getter
    private boolean loaded = false;

    public abstract void resume();
    public abstract void pause();
    public abstract void unload();

    public void disactive()
    {
        pause();
        if(!keepState)
        {
            destroy();
        }
    }

    public void active()
    {
        if(!loaded)
        {
            camera = new Camera();
            getParent().getResourcesDispatcher().dispatch(this);
            load();
            loaded = true;
        }
        else
        {
            if(!keepState)
            {
                camera = new Camera();
                getParent().getResourcesDispatcher().dispatch(this);
                load();
            }
        }
        resume();
    }

    public void asCurrent()
    {
        getParent().setScene(this);
    }

    @Override
    public final void destroy()
    {
        unload();
        getParent().getResourcesDispatcher().destroy(this);
    }
}
