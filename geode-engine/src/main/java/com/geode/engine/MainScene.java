package com.geode.engine;

import com.geode.engine.core.KeyManager;
import com.geode.engine.core.Scene;
import com.geode.engine.core.Window;
import com.geode.engine.dispatchers.Repository;
import com.geode.engine.entity.Square;
import com.geode.engine.utils.Colors;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class MainScene extends Scene<Main>
{


    Square object;

    Square filter;

    @Repository
    public MainResourceHolder resourceHolder;

    @Override
    public void load()
    {
        System.out.println("load scene");


        object = new Square(resourceHolder.bg);
        //object.setColor(Colors.RED);
        object.getTransform().setSize2D(Window.getWindow().getViewportSize());

        filter = new Square();
        filter.setColor(Colors.TRANSPARENT);
        filter.getTransform().setSize2D(object.getTransform().getSize2D());
    }

    @Override
    public void resume()
    {
        getParent().getWindow().setClearColor(new Vector4f(0f, 1f, 1f, 1f));
        getParent().getWindow().fullscreen();
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
        //System.out.println(getParent().getFps());
        object.update();
        filter.update();
        //filter.getColor().w += 0.05 * dt;
        // System.out.println(KeyManager.get().isKeyPressed(GLFW.GLFW_KEY_SPACE));
        if (KeyManager.get().isKeyMode(GLFW.GLFW_KEY_SPACE, KeyManager.Mods.CONTROL))
        {
            System.out.println("close");
            getParent().getWindow().shouldClose(true);
        }

        /*Vector2i mp = MouseManager.getMousePosition(getCamera());

        if (KeyManager.isKeyAlwaysPressed(GLFW.GLFW_KEY_UP))
            object.getTransform().addY(-300 * dt);
        if (KeyManager.isKeyAlwaysPressed(GLFW.GLFW_KEY_DOWN))
            object.getTransform().addY(300 * dt);
        if (KeyManager.isKeyAlwaysPressed(GLFW.GLFW_KEY_RIGHT))
            object.getTransform().addX(300 * dt);
        if (KeyManager.isKeyAlwaysPressed(GLFW.GLFW_KEY_LEFT))
            object.getTransform().addX(-300 * dt);*/
    }
}
