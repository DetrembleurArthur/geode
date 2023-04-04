package com.geode.engine.entity;

import com.geode.engine.graphics.Texture;
import com.geode.engine.graphics.prefabs.MeshFactory;

public class Rect extends SingleObject
{
    public Rect(Texture texture)
    {
        super(texture);
        setMesh(MeshFactory.rect(true, isTextured()));
    }

    public Rect()
    {
        this(null);
    }
}
