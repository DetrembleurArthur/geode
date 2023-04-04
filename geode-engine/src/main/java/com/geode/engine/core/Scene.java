package com.geode.engine.core;

import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.GameObjectPack;
import com.geode.engine.graphics.Camera;
import com.geode.engine.utils.LaterList;
import lombok.Getter;
import lombok.Setter;

public abstract class Scene<T extends Application> implements Manageable
{
    @Getter
    private Camera camera;

    @Getter
    @Setter
    private boolean keepState = false;

    @Getter
    @Setter
    private T parent;

    @Getter
    private boolean loaded = false;

    @Getter
    private final LaterList<GameObject> gameObjects = new LaterList<>();

    public abstract void resume();

    public abstract void pause();

    public abstract void unload();

    public void disactive()
    {
        pause();
        if (!keepState)
        {
            destroy();
        }
    }

    public void active()
    {
        if (!loaded)
        {
            camera = new Camera();
            getParent().getResourcesDispatcher().dispatch(this);
            load();
            loaded = true;
        } else
        {
            if (!keepState)
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
        destroyGameObjects();
        getParent().getResourcesDispatcher().destroy(this);
    }

    public void add(GameObject gameObject)
    {
        gameObjects.addLater(gameObject);
        if(gameObject instanceof GameObjectPack)
        {
            for(GameObject o : ((GameObjectPack)gameObject).getObjects())
                gameObjects.addLater(o);
        }
    }

    public void updateGameObjects()
    {
        gameObjects.sync();
        gameObjects.forEach(GameObject::update);
        gameObjects.removeIf(GameObject::destroyIfDirty);
    }

    private void destroyGameObjects()
    {
        gameObjects.forEach(GameObject::destroy);
        gameObjects.clear();
    }
}
