package com.geode.engine.entity.components;

import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.renderers.Renderer;
import lombok.Getter;
import lombok.Setter;

public class RenderComponent extends Component
{
    @Getter @Setter
    private Renderer renderer;

    public RenderComponent(GameObject parent, Renderer renderer)
    {
        super(parent, Component.MIN_PRIORITY);
        this.renderer = renderer;
    }

    @Override
    public void update()
    {
        renderer.render(getParent());
    }
}
