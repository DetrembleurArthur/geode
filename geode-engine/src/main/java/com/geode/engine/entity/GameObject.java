package com.geode.engine.entity;

import com.geode.engine.entity.components.animation.AnimationComponent;
import com.geode.engine.entity.components.Component;
import com.geode.engine.entity.components.MovementsComponent;
import com.geode.engine.entity.components.collision.CollisionComponent;
import com.geode.engine.entity.components.event.EventComponent;
import com.geode.engine.entity.components.property.PropertyComponent;
import com.geode.engine.graphics.Mesh;
import com.geode.engine.graphics.Texture;
import com.geode.engine.utils.Colors;
import com.geode.engine.utils.LaterList;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class GameObject implements Updatable
{
    private final LaterList<Component> components;

    @Getter
    private final Transform transform;

    @Getter @Setter
    private boolean dirty = false;

    @Getter @Setter
    private Mesh mesh;

    @Getter
    private Texture texture;

    private Vector4f color = new Vector4f(Colors.WHITE);

    public GameObject()
    {
        components = new LaterList<>();
        transform = new Transform();
    }

    public Component addComponent(Component component)
    {
        components.addLater(component);
        return component;
    }



    public void removeComponent(Class<? extends Component> componentClass)
    {
        components.removeIf(component -> component.getClass().equals(componentClass));
    }

    public <T extends Component> T getComponent(Class<T> componentClass)
    {
        for(Component component : components)
        {
            if(component.getClass().equals(componentClass))
            {
                return (T) component;
            }
        }
        for(Component component : components.getAddLaterList())
        {
            if(component.getClass().equals(componentClass))
            {
                return (T) component;
            }
        }
        return null;
    }

    public <T extends Component> T getOrCreateComponent(Class<T> _class)
    {
        T component = getComponent(_class);
        try
        {
            component = component != null ? component :
                    (T) addComponent(
                            _class.getConstructor(GameObject.class, Integer.class)
                                    .newInstance(this, Component.DEFAULT_PRIORITY));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return component;
    }

    public MovementsComponent move_c()
    {
        return getOrCreateComponent(MovementsComponent.class);
    }

    public AnimationComponent anim_c()
    {
        return getOrCreateComponent(AnimationComponent.class);
    }

    public PropertyComponent properties_c()
    {
        return getOrCreateComponent(PropertyComponent.class);
    }

    public CollisionComponent collision_c()
    {
        return getOrCreateComponent(CollisionComponent.class);
    }

    public EventComponent event_c()
    {
        return getOrCreateComponent(EventComponent.class);
    }

    @Override
    public final void update()
    {
        for(Component component : components)
        {
            component.update();
        }
        if(components.isMustBeUpdated())
        {
            components.sync();
            components.sort((o1, o2) -> o1.getPriority() < o2.getPriority() ? -1 : 1);
        }
    }

    public void destroy()
    {

    }

    public boolean isTextured()
    {
        return texture != null;
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
        if(texture != null)
        {
            transform.setSize2D(texture.getDimension());
        }
    }

    public void fitAsTexture()
    {
        Vector2f dimensions = getTexture().getDimension();
        getTransform().setSize2D(dimensions);
    }

    public void setColor(Vector4f color)
    {
        this.color = new Vector4f(color);
    }

    public Vector4f getColor()
    {
        return new Vector4f(color);
    }

    public Vector4f getColorRef()
    {
        return color;
    }

    public float getRed()
    {
        return color.x;
    }

    public void setRed(float red)
    {
        color.x = red;
    }

    public float getGreen()
    {
        return color.y;
    }

    public void setGreen(float green)
    {
        color.y = green;
    }

    public float getBlue()
    {
        return color.z;
    }

    public void setBlue(float blue)
    {
        color.z = blue;
    }

    public float getAlpha()
    {
        return color.w;
    }

    public void setAlpha(float alpha)
    {
        color.w = alpha;
    }

    public static boolean destroyIfDirty(GameObject gameObject)
    {
        if(gameObject.isDirty())
            gameObject.destroy();
        return gameObject.isDirty();
    }
}
