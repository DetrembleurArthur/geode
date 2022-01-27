package com.geode.engine;

import com.geode.engine.system.KeyManager;
import com.geode.engine.system.Scene;
import com.geode.engine.system.Window;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class MainScene extends Scene<Main>
{
    float[] vertex = new float[]{
        0.5f, -0.5f, 0.0f,        1.0f, 0.0f, 0.0f, 1.0f,
        -0.5f, 0.5f, 0.0f,        0.0f, 1.0f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f,        0.0f, 0.0f, 1.0f, 1.0f,
        -0.5f, -0.5f, 0.0f,        1.0f, 1.0f, 0.0f, 1.0f,
    };

    @Override
    public void load()
    {
        System.out.println("load scene");
        getParent().getWindow().setClearColor(new Vector4f(0f, 1f, 1f, 1f));
    }

    @Override
    public void update(float dt)
    {
        if (getParent().getKeyManager().isKeyMode(GLFW.GLFW_KEY_SPACE, KeyManager.Mods.ALT))
        {
            getParent().secondaryScene.asCurrent();
        }
    }

    @Override
    public void draw(Window window)
    {

    }

    @Override
    public void destroy()
    {
        System.out.println("destroy");
    }
}
