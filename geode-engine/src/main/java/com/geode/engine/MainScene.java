package com.geode.engine;

import com.geode.engine.core.MouseManager;
import com.geode.engine.core.Scene;
import com.geode.engine.dispatchers.Repository;
import com.geode.engine.entity.Rect;
import com.geode.engine.entity.components.event.MouseEnteredEvent;
import com.geode.engine.utils.Colors;
import org.joml.Vector4f;


public class MainScene extends Scene<Main>
{
    @Repository
    public MainResourceHolder resourceHolder;
    public Rect obj1;
    public Rect obj2;


    @Override
    public void load()
    {
        System.out.println("load scene");
        obj1 = new Rect(resourceHolder.blob);
        obj1.getTransform().setSize2D(100, 100);
        obj1.getTransform().setCenterOrigin();
        obj1.getTransform().center();
        obj1.setColor(Colors.GREEN);
       // obj1.collision_c().setDynamic(false);
        add(obj1);
        obj2 = new Rect(resourceHolder.blob);
        obj2.getTransform().setSize2D(100, 100);
        obj2.getTransform().setCenterOrigin();
        obj2.getTransform().setPosition2D(100, 50);
        obj2.setColor(Colors.BLUE);
        add(obj2);

        obj1.event_c().onCollision(sender -> System.out.println("collision"), obj2);



    }


    @Override
    public void update(float dt)
    {
        updateGameObjects();
        obj1.properties_c().position2D().set(MouseManager.getMousePositionf(getCamera()));
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
