package com.geode.engine;

import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.components.RenderComponent;
import com.geode.engine.graphics.Mesh;
import com.geode.engine.graphics.Renderer;
import com.geode.engine.graphics.Shader;
import com.geode.engine.system.KeyManager;
import com.geode.engine.system.Scene;
import com.geode.engine.system.Window;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class MainScene extends Scene<Main>
{
    float[] vertex = new float[]{
        0,0, 0.0f,  0,0,
        0,1, 0.0f, 0,1,
       1,1, 0.0f,    1,1,
        1,0, 0.0f,     1,0,
    };

    GameObject object;

    @Override
    public void load()
    {
        System.out.println("load scene");
        getParent().getWindow().setClearColor(new Vector4f(0f, 1f, 1f, 1f));

        object = new GameObject();
        object.setTexture(getParent().texture);
        object.setMesh(new Mesh(vertex, new int[]{0,1,2,0,2,3}));
        object.getTransform().setSize(new Vector3f(200, 200, 1));
        object.addComponent(new RenderComponent(object, new Renderer(Shader.DEFAULT, getCamera())));
    }

    @Override
    public void update(float dt)
    {
        if (getParent().getKeyManager().isKeyMode(GLFW.GLFW_KEY_SPACE, KeyManager.Mods.ALT))
        {
            getParent().secondaryScene.asCurrent();
        }
        object.update();
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
