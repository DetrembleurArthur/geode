package com.geode.engine.entity;

import com.geode.engine.entity.components.Component;
import com.geode.engine.graphics.Mesh;
import com.geode.engine.graphics.Texture;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class GameObject implements Updatable
{
    private final ArrayList<Component> components;

    @Getter
    private final Transform transform;

    @Getter @Setter
    private Mesh mesh;

    @Getter @Setter
    private Texture texture;

    public GameObject()
    {
        components = new ArrayList<>();
        transform = new Transform();
    }

    public void addComponent(Component component)
    {
        for(Component c : components)
        {
            if(c.getClass().equals(component.getClass()))
                return;
        }
        components.add(component);
        components.sort((o1, o2) -> o1.getPriority() < o2.getPriority() ? -1 : 1);
    }

    public void removeComponent(Class<? extends Component> componentClass)
    {
        components.removeIf(component -> component.getClass().equals(componentClass));
    }

    @Override
    public final void update()
    {
        for(Component component : components)
        {
            component.update();
        }
    }
}
