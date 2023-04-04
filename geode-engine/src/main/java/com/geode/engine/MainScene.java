package com.geode.engine;

import com.geode.engine.core.Application;
import com.geode.engine.core.Scene;
import com.geode.engine.dispatchers.Repository;
import com.geode.engine.entity.Rect;
import com.geode.engine.graphics.ui.text.Text;
import com.geode.engine.timer.SyncTimer;
import com.geode.engine.utils.Colors;
import org.joml.Vector2f;
import org.joml.Vector4f;


public class MainScene extends Scene<Main>
{
    @Repository
    public MainResourceHolder resourceHolder;

    public Rect rect1;
    public Rect rect2;

    public Text text;


    @Override
    public void load()
    {
        System.out.println("load scene");


        showFps();
    }


    @Override
    public void update(float dt)
    {
        updateGameObjects();
    }

    @Override
    public void resume()
    {
        getParent().getWindow().setClearColor(new Vector4f(0f, 1f, 1f, 1f));
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void unload()
    {

    }
}
