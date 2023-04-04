package com.geode.engine.core;

import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.GameObjectPack;
import com.geode.engine.entity.components.script.Script;
import com.geode.engine.graphics.Camera;
import com.geode.engine.graphics.ui.text.Text;
import com.geode.engine.utils.Colors;
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

    private Text fpsLabel;

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

    public void del(GameObject gameObject)
    {
        gameObjects.removeLater(gameObject);
        if(gameObject instanceof GameObjectPack)
        {
            for(GameObject o : ((GameObjectPack)gameObject).getObjects())
            {
                gameObjects.removeLater(o);
                o.destroy();
            }
        }
        gameObject.destroy();
    }

    public void showFps()
    {
        if(fpsLabel == null || fpsLabel.isDirty())
        {
            fpsLabel = new Text(Application.getApplication().engineFont, "FPS: ");
            fpsLabel.setTextHeight(20);
            fpsLabel.setColor(Colors.BLACK);
            fpsLabel.getTransform().addPosition2D(10, 10);
            fpsLabel.script_c().addScript(new Script<>()
                    .setAction(param -> fpsLabel.setText("FPS: " + Application.getApplication().getFps())));
        }
        if(!gameObjects.contains(fpsLabel))
            add(fpsLabel);
    }

    public void hideFps()
    {
        if(fpsLabel != null && gameObjects.contains(fpsLabel))
        {
            fpsLabel.setDirty(true);
            del(fpsLabel);
        }
    }

    public void toggleFps()
    {
        if(fpsLabel != null && !fpsLabel.isDirty())
            hideFps();
        else
            showFps();
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
