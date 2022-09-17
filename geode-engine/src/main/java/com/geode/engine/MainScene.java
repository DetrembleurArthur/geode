package com.geode.engine;

import com.geode.engine.core.MouseManager;
import com.geode.engine.core.Scene;
import com.geode.engine.core.Time;
import com.geode.engine.dispatchers.Repository;
import com.geode.engine.entity.Square;
import com.geode.engine.entity.components.property.PropertyComponent;
import org.joml.Vector4f;


public class MainScene extends Scene<Main>
{
    @Repository
    public MainResourceHolder resourceHolder;
    public Square obj1;
    public Square obj2;


    @Override
    public void load()
    {
        System.out.println("load scene");
        obj1 = new Square(resourceHolder.blob);
        obj1.getTransform().setSize2D(100, 100);
        obj1.getTransform().setCenterOrigin();
        obj1.getTransform().center();
        add(obj1);

        /*obj1.anim_c().toXProperty()
                .from(100)
                .to(1000)
                .delay(3000f)
                .getAction().start();*/

        obj2 = new Square(resourceHolder.blob);
        obj2.getTransform().setSize2D(100, 100);
        obj2.getTransform().setCenterOrigin();
        obj2.getTransform().setPosition2D(50, 50);
        add(obj2);


        obj1.properties_c().x().bind(obj2.properties_c().x(), integer -> integer/2);
        obj1.properties_c().y().bind(obj2.properties_c().y(), value -> value/2);

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
            //move_c.lookAt(getCamera().getMousePositionf());
            //xProperty.set(getCamera().getMousePositionf().x);
            //obj1.properties_c().width().set(obj1.getTransform().getWidth() + 1);

            /*obj1.move_c().move(10, 0);
            obj1.properties_c().x().set(obj1.getTransform().getX());*/

            obj1.move_c().moveProp(50, 50);
        }
        updateGameObjects();
    }
}
