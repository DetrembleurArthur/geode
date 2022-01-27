package com.geode.engine.entity;

import java.util.ArrayList;

public class GameObject implements Updatable
{
    private ArrayList<Component> components;

    public GameObject()
    {
        components = new ArrayList<>();
    }

    public void addComponent(Component component)
    {
        for(Component c : components)
        {
            if(c.getClass().equals(component.getClass()))
                return;
        }
        components.add(component);
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
