package com.geode.engine.entity.components.property;

import com.geode.binding.FieldPropertiesScheme;
import com.geode.binding.NotifyProperty;
import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.Transform;
import com.geode.engine.entity.components.Component;
import org.joml.Vector2f;

import java.util.HashMap;

public class PropertyComponent extends Component
{
    private static final HashMap<Class<?>, FieldPropertiesScheme<?>> propertiesSchemeHashMap;

    static
    {
        propertiesSchemeHashMap = new HashMap<>();
        setup(Transform.class);
    }

    public static void setup(Class<?> _class)
    {
        propertiesSchemeHashMap.putIfAbsent(_class, new FieldPropertiesScheme<>(_class));
    }

    private final HashMap<String, NotifyProperty<?>> properties = new HashMap<>();

    public PropertyComponent(GameObject parent, Integer priority)
    {
        super(parent, priority);
    }

    public NotifyProperty<?> enableProperty(String name)
    {
        var schemes = propertiesSchemeHashMap.get(Transform.class);
        var property = schemes.get(name).create(getParent().getTransform());
        properties.put(name, property);
        return property;
    }

    private <T> NotifyProperty<T> _Property(String name)
    {
        var property = properties.get(name);
        if(property == null)
            property = enableProperty(name);
        return (NotifyProperty<T>) property;
    }

    public NotifyProperty<Float> x()
    {
        return _Property(PropertiesName.X);
    }

    public NotifyProperty<Float> y()
    {
        return _Property(PropertiesName.Y);
    }

    public NotifyProperty<Float> width()
    {
        return _Property(PropertiesName.WIDTH);
    }

    public NotifyProperty<Float> height()
    {
        return _Property(PropertiesName.HEIGHT);
    }

    public NotifyProperty<Vector2f> position2D()
    {
        return _Property(PropertiesName.POSITION2D);
    }


    @Override
    public void update()
    {

    }
}
