package com.geode.engine;

import com.geode.engine.dispatchers.Repository;
import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.components.RenderComponent;
import com.geode.engine.graphics.*;
import com.geode.engine.core.*;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class MainScene extends Scene<Main>
{
    float[] positions = new float[]{
        0f, 0f, 0f,
        0f, 1f, 0f,
        1f, 1f, 0f,
        1f, 0f, 0f,
    };

    float[] colors = new float[]{
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,
    };

    float[] uvs = new float[]{
            0, 0,
            0, 1,
            1, 1,
            1, 0,
    };

    int[] indices = new int[]{
            0, 1, 2,
            0, 2, 3
    };

    GameObject object;

    @Repository
    public MainResourceHolder resourceHolder;

    @Override
    public void load()
    {
        System.out.println("load scene");


        object = new GameObject();
        object.setTexture(resourceHolder.blob);

        MeshContext context = new MeshContext();
        MeshContext.Attribute positionsAttr = MeshContext.Attribute.builder().data(positions).size(3).build();
        MeshContext.Attribute colorsAttr = MeshContext.Attribute.builder().data(colors).size(4).build();
        MeshContext.Attribute uvsAttr = MeshContext.Attribute.builder().data(uvs).size(2).build();
        object.setMesh(new Mesh(context.addAttribute(positionsAttr).addAttribute(colorsAttr).addAttribute(uvsAttr), indices));
        Renderer renderer = new Renderer(Shader.DEFAULT, getCamera());
        object.addComponent(new RenderComponent(object, renderer));

        object.getTransform().getSize().mul(10);
        object.getTransform().setPosition(new Vector3f(500,500,0));
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
        Vector2i mp = MouseManager.getMousePosition(getCamera());
        object.update();
        object.getTransform().setPosition(new Vector3f(mp.x, mp.y, 0));

        if(KeyManager.keyManager.isKeyReleased(GLFW.GLFW_KEY_SPACE))
            //getParent().getWindow().fullscreen();
            getCamera().focus(new Vector2f(mp.x, mp.y));
        if(KeyManager.keyManager.isKeyReleased(GLFW.GLFW_KEY_ENTER))
            getParent().secondaryScene.asCurrent();
    }
}
