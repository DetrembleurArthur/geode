package com.geode.engine.entity.components.property;

import com.geode.engine.binding.Converter;
import com.geode.engine.binding.FieldPropertiesScheme;
import com.geode.engine.binding.NotifyProperty;
import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.Transform;
import com.geode.engine.entity.components.Component;
import com.geode.engine.entity.components.MovementsComponent;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.HashMap;

public class PropertyComponent extends Component
{
    private static final HashMap<Class<?>, FieldPropertiesScheme<?>> propertiesSchemeHashMap;

    static
    {
        propertiesSchemeHashMap = new HashMap<>();
        setup(Transform.class);
        setup(GameObject.class);
        setup(MovementsComponent.class);
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

    public NotifyProperty<?> enableProperty(Class<?> _class, String name)
    {
        var schemes = propertiesSchemeHashMap.get(_class);
        Object obj = null;
        if (Transform.class.equals(_class))
        {
            obj = getParent().getTransform();
        } else if (GameObject.class.equals(_class))
        {
            obj = getParent();
        } else if (MovementsComponent.class.equals(_class))
        {
            obj = getParent().move_c();
        }
        NotifyProperty<?> property = schemes.get(name).create(obj);
        properties.put(name, property);
        return property;
    }

    private <T> NotifyProperty<T> _Property(Class<?> _class, String name)
    {
        var property = properties.get(name);
        if(property == null)
        {
            property = enableProperty(_class, name);
            toggleNew();
        }
        return (NotifyProperty<T>) property;
    }

    public NotifyProperty<Float> x()
    {
        NotifyProperty<Float> prop = _Property(Transform.class, PropertiesName.X);
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
        NotifyProperty<Float> prop = _Property(Transform.class, PropertiesName.Y);
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
        NotifyProperty<Float> prop = _Property(Transform.class, PropertiesName.WIDTH);
        if(isNew)
        {
            toggleNew();
            Converter<Vector2f, Float> converter1 = value -> value.x;
            Converter<Float, Vector2f> converter2 = value -> new Vector2f(value, size2D().get().y);
            prop.link(size2D(), converter2, converter1);
        }
        return prop;
    }

    public NotifyProperty<Float> height()
    {
        NotifyProperty<Float> prop = _Property(Transform.class, PropertiesName.HEIGHT);
        if(isNew)
        {
            toggleNew();
            Converter<Vector2f, Float> converter1 = value -> value.y;
            Converter<Float, Vector2f> converter2 = value -> new Vector2f(size2D().get().x, value);
            prop.link(size2D(), converter2, converter1);
        }
        return prop;
    }

    public NotifyProperty<Vector2f> position2D()
    {
        NotifyProperty<Vector2f> prop = _Property(Transform.class, PropertiesName.POSITION2D);
        if(isNew)
        {
            toggleNew();
            Converter<Vector2f, Float> converter1 = value -> value.x;
            Converter<Float, Vector2f> converter2 = value -> new Vector2f(value, prop.get().y);
            prop.link(x(), converter1, converter2);
            converter1 = value -> value.y;
            converter2 = value -> new Vector2f(prop.get().x, value);
            prop.link(y(), converter1, converter2);
        }
        return prop;
    }

    public NotifyProperty<Vector2f> size2D()
    {
        NotifyProperty<Vector2f> prop = _Property(Transform.class, PropertiesName.SIZE2D);
        if(isNew)
        {
            toggleNew();
            Converter<Vector2f, Float> converter1 = value -> value.x;
            Converter<Float, Vector2f> converter2 = value -> new Vector2f(value, prop.get().y);
            prop.link(width(), converter1, converter2);
            converter1 = value -> value.y;
            converter2 = value -> new Vector2f(prop.get().x, value);
            prop.link(height(), converter1, converter2);
        }
        return prop;
    }

    public NotifyProperty<Float> angle()
    {
        return _Property(Transform.class, PropertiesName.ANGLE);
    }

    public NotifyProperty<Vector4f> color()
    {
        NotifyProperty<Vector4f> prop = _Property(GameObject.class, PropertiesName.COLOR);
        if(isNew)
        {
            toggleNew();
            Converter<Vector4f, Float> converter1 = value -> value.x;
            Converter<Float, Vector4f> converter2 = value -> {
                Vector4f temp = prop.get();
                return new Vector4f(value, temp.y, temp.z, temp.w);
            };
            prop.link(red(), converter1, converter2);
            converter1 = value -> value.y;
            converter2 = value -> {
                Vector4f temp = prop.get();
                return new Vector4f(temp.x, value, temp.z, temp.w);
            };
            prop.link(green(), converter1, converter2);
            converter1 = value -> value.z;
            converter2 = value -> {
                Vector4f temp = prop.get();
                return new Vector4f(temp.x, temp.y, value, temp.w);
            };
            prop.link(blue(), converter1, converter2);
            converter1 = value -> value.w;
            converter2 = value -> {
                Vector4f temp = prop.get();
                return new Vector4f(temp.x, temp.y, temp.z, value);
            };
            prop.link(alpha(), converter1, converter2);
        }
        return prop;
    }

    public NotifyProperty<Float> red()
    {
        NotifyProperty<Float> prop = _Property(GameObject.class, PropertiesName.RED);
        if(isNew)
        {
            toggleNew();
            Converter<Vector4f, Float> converter1 = value -> value.x;
            Converter<Float, Vector4f> converter2 = value -> {
                Vector4f temp = color().get();
                return new Vector4f(value, temp.y, temp.z, temp.w);
            };
            prop.link(color(), converter2, converter1);
        }
        return prop;
    }

    public NotifyProperty<Float> green()
    {
        NotifyProperty<Float> prop = _Property(GameObject.class, PropertiesName.GREEN);
        if(isNew)
        {
            toggleNew();
            Converter<Vector4f, Float> converter1 = value -> value.y;
            Converter<Float, Vector4f> converter2 = value -> {
                Vector4f temp = color().get();
                return new Vector4f(temp.x, value, temp.z, temp.w);
            };
            prop.link(color(), converter2, converter1);
        }
        return prop;
    }

    public NotifyProperty<Float> blue()
    {
        NotifyProperty<Float> prop = _Property(GameObject.class, PropertiesName.BLUE);
        if(isNew)
        {
            toggleNew();
            Converter<Vector4f, Float> converter1 = value -> value.z;
            Converter<Float, Vector4f> converter2 = value -> {
                Vector4f temp = color().get();
                return new Vector4f(temp.x, temp.y, value, temp.w);
            };
            prop.link(color(), converter2, converter1);
        }
        return prop;
    }

    public NotifyProperty<Float> alpha()
    {
        NotifyProperty<Float> prop = _Property(GameObject.class, PropertiesName.ALPHA);
        if(isNew)
        {
            toggleNew();
            Converter<Vector4f, Float> converter1 = value -> value.w;
            Converter<Float, Vector4f> converter2 = value -> {
                Vector4f temp = color().get();
                return new Vector4f(temp.x, temp.y, temp.z, value);
            };
            prop.link(color(), converter2, converter1);
        }
        return prop;
    }

    public NotifyProperty<Vector2f> speed()
    {
        NotifyProperty<Vector2f> prop = _Property(MovementsComponent.class, PropertiesName.SPEED);
        if(isNew)
        {
            toggleNew();
            Converter<Vector2f, Float> converter1 = value -> value.x;
            Converter<Float, Vector2f> converter2 = value -> new Vector2f(value, prop.get().y);
            prop.link(speedX(), converter1, converter2);
            converter1 = value -> value.y;
            converter2 = value -> new Vector2f(prop.get().x, value);
            prop.link(speedY(), converter1, converter2);
        }
        return prop;
    }

    public NotifyProperty<Float> speedX()
    {
        NotifyProperty<Float> prop = _Property(MovementsComponent.class, PropertiesName.SPEEDX);
        if(isNew)
        {
            toggleNew();
            Converter<Vector2f, Float> converter1 = value -> value.x;
            Converter<Float, Vector2f> converter2 = value -> new Vector2f(value, speed().get().y);
            prop.link(speed(), converter2, converter1);
        }
        return prop;
    }

    public NotifyProperty<Float> speedY()
    {
        NotifyProperty<Float> prop = _Property(MovementsComponent.class, PropertiesName.SPEEDY);
        if(isNew)
        {
            toggleNew();
            Converter<Vector2f, Float> converter1 = value -> value.y;
            Converter<Float, Vector2f> converter2 = value -> new Vector2f(speed().get().x, value);
            prop.link(speed(), converter2, converter1);
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
