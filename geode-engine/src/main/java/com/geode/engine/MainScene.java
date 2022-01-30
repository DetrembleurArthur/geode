package com.geode.engine;

import com.geode.engine.core.Scene;
import com.geode.engine.core.Time;
import com.geode.engine.core.Window;
import com.geode.engine.dispatchers.Repository;
import com.geode.engine.utils.Colors;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class MainScene extends Scene<Main>
{
    @Repository
    public MainResourceHolder resourceHolder;


    @Override
    public void load()
    {
        System.out.println("load scene");

    }

    @Override
    public void resume()
    {
        getParent().getWindow().setClearColor(new Vector4f(0f, 1f, 1f, 1f));
        //getParent().getWindow().fullscreen();
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void unload()
    {

    }

    @Override
    public void update(float dt)
    {
        updateGameObjects();
    }
}
