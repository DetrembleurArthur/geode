package com.geode.engine.entity;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transform
{
    @Getter @Setter private Vector3f position;
    @Getter @Setter private Vector3f size;
    @Getter @Setter private Vector3f rotation;
    @Getter @Setter private Vector3f origin;

    public Transform()
    {
        position = new Vector3f();
        size = new Vector3f(1, 1, 1);
        rotation = new Vector3f();
        origin = new Vector3f();
    }

    public final Matrix4f getModel()
    {
        return new Matrix4f().identity()
                .translate(new Vector3f(position))
                .rotateX(rotation.x).rotateY(rotation.y).rotateY(rotation.z)
                .scale(size);
    }

    public void setSize2D(float width, float height)
    {
        size.x = width;
        size.y = height;
    }

    public void setSize2D(Vector2f size2D)
    {
        setSize2D(size2D.x, size2D.y);
    }

    public void setWidth(float width)
    {
        size.x = width;
    }

    public void setHeight(float height)
    {
        size.y = height;
    }

    public Vector2f getSize2D()
    {
        return new Vector2f(size.x, size.y);
    }

    public float getWidth()
    {
        return size.x;
    }

    public float getHeight()
    {
        return size.y;
    }

    public void setPosition2D(float x, float y)
    {
        position.x = x;
        position.y = y;
    }

    public void setPosition2D(Vector2f position2D)
    {
        setPosition2D(position2D.x, position2D.y);
    }

    public void setX(float x)
    {
        position.x = x;
    }

    public void setY(float y)
    {
        position.y = y;
    }

    public Vector2f getPosition2D()
    {
        return new Vector2f(position.x, position.y);
    }

    public float getX()
    {
        return position.x;
    }

    public float getY()
    {
        return position.y;
    }

    public void addPosition2D(float x, float y)
    {
        setPosition2D(getX() + x, getY() + y);
    }

    public void addPosition2D(Vector2f position)
    {
        addPosition2D(position.x, position.y);
    }

    public void addX(float x)
    {
        setX(getX() + x);
    }

    public void addY(float y)
    {
        setY(getY() + y);
    }
}
