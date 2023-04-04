package com.geode.engine.entity;

import com.geode.engine.core.Application;
import com.geode.engine.utils.Colors;
import lombok.Getter;

import java.util.ArrayList;

public class GameObjectPack extends GameObject
{
    @Getter
    private final ArrayList<GameObject> objects = new ArrayList<>();

    public GameObjectPack()
    {
        super();
        setColor(Colors.TRANSPARENT);
    }

    @Override
    public void destroy()
    {
        for(GameObject object : objects)
        {
            object.destroy();
            Application.getApplication().getScene().getGameObjects().removeLater(objects);
        }
        super.destroy();
    }

    public void add(GameObject object)
    {
        objects.add(object);
    }
}
