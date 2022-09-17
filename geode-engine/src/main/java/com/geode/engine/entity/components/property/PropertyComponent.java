package com.geode.engine.entity.components.property;

import com.geode.engine.binding.Converter;
import com.geode.engine.binding.FieldPropertiesScheme;
import com.geode.engine.binding.NotifyProperty;
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
    private boolean isNew = false;

    public PropertyComponent(GameObject parent, Integer priority)
    {
        super(parent, priority);
    }

    public NotifyProperty<?> enableProperty(String name)
    {
        var schemes = propertiesSchemeHashMap.get(Transform.class);
        NotifyProperty<?> property = schemes.get(name).create(getParent().getTransform());
        properties.put(name, property);
        return property;
    }

    private <T> NotifyProperty<T> _Property(String name)
    {
        var property = properties.get(name);
        if(property == null)
        {
            property = enableProperty(name);
            toggleNew();
        }
        return (NotifyProperty<T>) property;
    }

    public NotifyProperty<Float> x()
    {
        NotifyProperty<Float> prop = _Property(PropertiesName.X);
        if(isNew)
        {
            toggleNew();
            Converter<Vector2f, Float> converter1 = value -> value.x;
            Converter<Float, Vector2f> converter2 = value -> new Vector2f(value, position2D().get().y);
            prop.link(position2D(), converter2, converter1);
        }
        return prop;
    }

    public NotifyProperty<Float> y()
    {
        NotifyProperty<Float> prop = _Property(PropertiesName.Y);
        if(isNew)
        {
            toggleNew();
            Converter<Vector2f, Float> converter1 = value -> value.y;
            Converter<Float, Vector2f> converter2 = value -> new Vector2f(position2D().get().x, value);
            prop.link(position2D(), converter2, converter1);
        }
        return prop;
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
        NotifyProperty<Vector2f> prop = _Property(PropertiesName.POSITION2D);
        if(isNew)
        {
            toggleNew();
            Converter<Vector2f, Float> converter1 = value -> value.x;
            Converter<Float, Vector2f> converter2 = value -> new Vector2f(value, prop.get().y);
            prop.link(x(), converter1, converter2);
        }
        return prop;
    }

    private void toggleNew()
    {
        isNew = !isNew;
    }


    @Override
    public void update()
    {

    }
}
