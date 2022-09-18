package com.geode.engine;

import com.geode.engine.core.MouseManager;
import com.geode.engine.core.Scene;
import com.geode.engine.dispatchers.Repository;
import com.geode.engine.entity.Line;
import com.geode.engine.entity.Square;
import com.geode.engine.tweening.TFunc;
import com.geode.engine.utils.Colors;
import org.joml.Vector2f;
import org.joml.Vector4f;


public class MainScene extends Scene<Main>
{
    @Repository
    public MainResourceHolder resourceHolder;
    public Square obj1;
    public Square obj2;
    public Square obj3;
    public Line line;
    public Line lineX;
    public Line lineY;


    @Override
    public void load()
    {
        System.out.println("load scene");
        obj1 = new Square(resourceHolder.blob);
        obj1.getTransform().setSize2D(100, 100);
        obj1.getTransform().setCenterOrigin();
        obj1.getTransform().center();
        obj1.setColor(Colors.GREEN);
        add(obj1);
        obj2 = new Square(resourceHolder.blob);
        obj2.getTransform().setSize2D(100, 100);
        obj2.getTransform().setCenterOrigin();
        obj2.getTransform().setPosition2D(100, 50);
        obj2.setColor(Colors.BLUE);
        add(obj2);

        obj3 = new Square(resourceHolder.blob);
        obj3.getTransform().setSize2D(100, 100);
        obj3.getTransform().setCenterOrigin();
        obj3.getTransform().setPosition2D(100, 50);
        obj3.setColor(Colors.RED);
        add(obj3);

        line = new Line();
        line.setWeight(3);
        line.setLimit(50);
        line.addPoint(new Vector2f());
        add(line);

        lineX = new Line();
        lineX.setup(2);
        lineX.setWeight(3);
        lineX.setColor(Colors.BLUE);
        add(lineX);

        lineY = new Line();
        lineY.setup(2);
        lineY.setWeight(3);
        lineY.setColor(Colors.RED);
        add(lineY);


        obj1.properties_c().x().bind(obj2.properties_c().x());
        obj1.properties_c().y().bind(obj3.properties_c().y());


        /*obj1.anim_c().toRedProperty().from(obj1.getRed()).to(1f).delay(3000f).back(true).func(TFunc.EASE_IN_CUBIC).start();
        obj1.properties_c().red().bind(obj2.properties_c().red());*/
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
        if (MouseManager.getMouseManager().isRightButtonPressed())
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

            //obj1.move_c().moveProp(50, 50);
            line.addPoint(getCamera().getMousePositionf());
        }
        if(MouseManager.getMouseManager().isLeftButtonReleased())
        {
            obj1.anim_c().followLineProperty(line, 3000f);
        }
        lineX.setPoint(0, obj2.getTransform().getPosition2D());
        lineX.setPoint(1, obj1.getTransform().getPosition2D());
        lineY.setPoint(0, obj3.getTransform().getPosition2D());
        lineY.setPoint(1, obj1.getTransform().getPosition2D());
        updateGameObjects();
        //obj2.properties_c().position2D().set(line.getPoint(0));
    }
}
