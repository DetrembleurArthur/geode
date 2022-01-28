package com.geode.engine;

import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.components.RenderComponent;
import com.geode.engine.graphics.Mesh;
import com.geode.engine.graphics.MeshContext;
import com.geode.engine.graphics.Renderer;
import com.geode.engine.graphics.Shader;
import com.geode.engine.system.KeyManager;
import com.geode.engine.system.Scene;
import com.geode.engine.system.Window;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class MainScene extends Scene<Main>
{
    float[] positions = new float[]{
        0.5f, -0.5f, 0f,
        -0.5f, 0.5f, 0f,
        0.5f, 0.5f, 0f,
        -0.5f, -0.5f, 0f,
    };

    float[] colors = new float[]{
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 1f, 0f, 1f,
    };

    int[] indices = new int[]{
            2, 1, 0,
            0, 1, 3
    };

    GameObject object;

    Renderer renderer = new Renderer(Shader.DEFAULT, getCamera());

    @Override
    public void load()
    {
        System.out.println("load scene");
        getParent().getWindow().setClearColor(new Vector4f(0f, 1f, 1f, 1f));

        object = new GameObject();
        //object.setTexture(getParent().texture);

        MeshContext context = new MeshContext();
        MeshContext.Attribute positionsAttr = MeshContext.Attribute.builder().data(positions).size(3).build();
        MeshContext.Attribute colorsAttr = MeshContext.Attribute.builder().data(colors).size(4).build();
        object.setMesh(new Mesh(context.addAttribute(positionsAttr).addAttribute(colorsAttr), indices));
        //object.getTransform().setSize(new Vector3f(200, 200, 1));
        object.addComponent(new RenderComponent(object, renderer));
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
