package com.geode.engine.system;

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
            camera = new Camera();
            load();
            loaded = true;
        }
        else
        {
            if(!keepState)
            {
                camera = new Camera();
                load();
            }
        }
    }

    public void asCurrent()
    {
        getParent().setScene(this);
    }
}
