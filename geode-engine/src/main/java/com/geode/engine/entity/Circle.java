package com.geode.engine.entity;

import com.geode.engine.graphics.Texture;
import com.geode.engine.graphics.prefabs.MeshFactory;
import lombok.Getter;

public class Circle extends SingleObject
{
    @Getter
    private int points;

    @Getter
    private float radius;

    public Circle(Texture texture, int points, float radius)
    {
        super(texture);
        setRadius(radius);
        setPoints(points);
    }

    public Circle(int points, float radius)
    {
        this(null, points, radius);
    }

    public void setPoints(int points)
    {
        setMesh(MeshFactory.circle(true, isTextured(), points));
        this.points = points;
    }

    public void setRadius(float radius)
    {
        this.radius = radius;
        getTransform().setSize2D(radius * 2, radius * 2);
    }
}
