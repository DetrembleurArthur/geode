package com.geode.engine.entity;

import com.geode.engine.graphics.Texture;
import com.geode.engine.graphics.prefabs.MeshFactory;

public class Square extends SingleObject
{
    public Square(Texture texture)
    {
        super(texture);
        setMesh(MeshFactory.rect(isTextured(), isTextured()));
    }

    public Square()
    {
        this(null);
    }
}
