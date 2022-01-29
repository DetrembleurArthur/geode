package com.geode.engine;

import com.geode.engine.core.KeyManager;
import com.geode.engine.core.Scene;
import com.geode.engine.core.Window;
import com.geode.engine.utils.Colors;
import org.lwjgl.glfw.GLFW;

public class SecondaryScene extends Scene<Main>
{
    @Override
    public void load()
    {

    }

    @Override
    public void resume()
    {
        getParent().getWindow().setClearColor(Colors.RED);
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
        if(getParent().getKeyManager().isKeyMode(GLFW.GLFW_KEY_SPACE, KeyManager.Mods.CONTROL))
        {
            getParent().mainScene.asCurrent();
        }
    }
}
