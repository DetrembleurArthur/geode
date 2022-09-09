package com.geode.engine;

import com.geode.engine.core.MouseManager;
import com.geode.engine.core.Scene;
import com.geode.engine.dispatchers.Repository;
import com.geode.engine.entity.Square;
import com.geode.engine.entity.components.MovementsComponent;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MainScene extends Scene<Main>
{
    @Repository
    public MainResourceHolder resourceHolder;
    public Square object;
    public MovementsComponent move_c;


    @Override
    public void load()
    {
        System.out.println("load scene");
        object = new Square(resourceHolder.blob);
        object.getTransform().setSize2D(100, 100);
        object.getTransform().setCenterOrigin();
        object.getTransform().center();
        move_c = object.enableMovementsComponent();
        add(object);

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
        //object.getTransform().setPosition2D(getCamera().getMousePositionf());
        if (MouseManager.getMouseManager().isLeftButtonPressed())
        {
            //object.getComponent(MovementsComponent.class).moveToward(getCamera().getMousePositionf(), 100);
            //move_c.rotate(new Vector3f(180,180,180));
            //move_c.rotateAround(getCamera().getMousePositionf(), new Vector2f(90, 260));
            //move_c.placeAround(getCamera().getMousePositionf(), 150, 270);
            //move_c.rotateToward(getCamera().getMousePositionf(), 360);
            move_c.lookAt(getCamera().getMousePositionf());
        }
        updateGameObjects();
    }
}
