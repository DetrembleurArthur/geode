package com.geode.engine.entity;

import com.geode.engine.entity.components.RenderComponent;
import com.geode.engine.graphics.Texture;
import com.geode.engine.graphics.renderers.DefaultRenderer;
import com.geode.engine.graphics.renderers.DefaultTextureRenderer;

public abstract class SingleObject extends GameObject
{
    public SingleObject(Texture texture)
    {
        super();
        setTexture(texture);
        addComponent(new RenderComponent(this, isTextured() ? new DefaultTextureRenderer() : new DefaultRenderer()));
    }

    public SingleObject()
    {
        this(null);
    }
}
