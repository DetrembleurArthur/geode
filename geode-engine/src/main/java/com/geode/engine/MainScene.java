package com.geode.engine;

import com.geode.binding.FieldPropertiesScheme;
import com.geode.binding.FieldPropertyScheme;
import com.geode.binding.NotifyProperty;
import com.geode.binding.Property;
import com.geode.engine.core.MouseManager;
import com.geode.engine.core.Scene;
import com.geode.engine.dispatchers.Repository;
import com.geode.engine.entity.Square;
import com.geode.engine.entity.Transform;
import com.geode.engine.entity.components.MovementsComponent;
import org.joml.Vector4f;


public class MainScene extends Scene<Main>
{
    @Repository
    public MainResourceHolder resourceHolder;
    public Square object;
    public MovementsComponent move_c;

    public FieldPropertiesScheme<Transform> propertiesScheme = new FieldPropertiesScheme<>(Transform.class);
    public FieldPropertyScheme<Float> xScheme = propertiesScheme.get("width");
    public FieldPropertyScheme<Float> yScheme = propertiesScheme.get("height");
    public NotifyProperty<Float> xProperty;
    public NotifyProperty<Float> yProperty;



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

        xProperty = xScheme.create(object.getTransform());
        yProperty = yScheme.create(object.getTransform());
        System.out.println(xProperty.get() + " " + yProperty.get());

        xProperty.bind(yProperty, aFloat -> aFloat * 0.5f);

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
            xProperty.set(getCamera().getMousePositionf().x);
            object.getTransform().setPosition2D(getCamera().getMousePositionf());
        }
        updateGameObjects();
    }
}
