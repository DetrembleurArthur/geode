package com.geode.engine;

import com.geode.engine.core.KeyManager;
import com.geode.engine.core.MouseManager;
import com.geode.engine.core.Scene;
import com.geode.engine.dispatchers.Repository;
import com.geode.engine.entity.Square;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class MainScene extends Scene<Main>
{


    Square object;

    @Repository
    public MainResourceHolder resourceHolder;

    @Override
    public void load()
    {
        System.out.println("load scene");


        object = new Square(resourceHolder.blob);
        //object.setColor(Colors.RED);
        object.getTransform().getSize().mul(10, 10, 1);
        object.getTransform().setPosition(new Vector3f(500, 500, 0));

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

    @Override
    public void update(float dt)
    {
        getCamera().getPosition().x += 200 * dt;
        System.out.println(getParent().getFps());
        Vector2i mp = MouseManager.getMousePosition(getCamera());
        object.update();
        if (KeyManager.isKeyAlwaysPressed(GLFW.GLFW_KEY_UP))
            object.getTransform().addY(-300 * dt);
        if (KeyManager.isKeyAlwaysPressed(GLFW.GLFW_KEY_DOWN))
            object.getTransform().addY(300 * dt);
        if (KeyManager.isKeyAlwaysPressed(GLFW.GLFW_KEY_RIGHT))
            object.getTransform().addX(300 * dt);
        if (KeyManager.isKeyAlwaysPressed(GLFW.GLFW_KEY_LEFT))
            object.getTransform().addX(-300 * dt);
    }
}
