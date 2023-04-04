package com.geode.engine;

import com.geode.engine.core.Application;
import com.geode.engine.core.Scene;
import com.geode.engine.core.Time;
import com.geode.engine.dispatchers.Repository;
import com.geode.engine.entity.Rect;
import com.geode.engine.entity.components.script.Script;
import com.geode.engine.utils.Colors;
import org.joml.Vector4f;

import java.awt.*;


public class MainScene extends Scene<Main>
{
    @Repository
    public MainResourceHolder resourceHolder;

    public Rect rect1;
    public Rect rect2;


    @Override
    public void load()
    {
        System.out.println("load scene");

        rect1 = new Rect(resourceHolder.blob);
        rect1.getTransform().setSize2D(100, 100);
        rect1.setColor(Colors.RED);
        rect1.event_c().enableMouseDragging(getCamera());
        add(rect1);

        rect2 = new Rect(resourceHolder.blob);
        rect2.getTransform().setCenterOrigin();
        rect2.getTransform().setSize2D(100, 100);
        rect2.setColor(Colors.BLUE);
        rect2.event_c().enableMouseDragging(getCamera());
        rect2.properties_c().angle().set(45.0f);
        add(rect2);


        rect1.script_c().addScript(new Script<Void>()
                .setAction(param -> {
                    if(rect1.collision_c().contains(rect2))
                    {
                        System.out.println("COLLISION");
                    }
                }));

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
