package com.geode.engine.entity;

import com.geode.engine.core.Application;
import com.geode.engine.core.Window;
import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix2f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transform
{
    @Setter
    private Vector3f position;
    private Vector3f size;
    @Setter
    private Vector3f rotation;
    private Vector3f origin;

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
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z))
                .translate(getOrigin().negate()) //pour la rotation autour de l'origine
                .scale(size);
    }

    public void setSize2D(float width, float height)
    {
        if(width == 0f)
            width = (float) 1e-10;
        if(height == 0f)
            height = (float) 1e-10;
        origin.mul(new Vector3f(width, height, 0).div(size.x, size.y, 1));
        size.x = width;
        size.y = height;
    }

    public void setSize2D(Vector2f size2D)
    {
        setSize2D(size2D.x, size2D.y);
    }

    public void setSize(float width, float height, float depth)
    {
        origin.mul(new Vector3f(width, height, depth).div(size.x, size.y, size.z));
        size.x = width;
        size.y = height;
        size.z = depth;
    }

    public void setSize(Vector3f size)
    {
        setSize(size.x, size.y, size.z);
    }

    public void setWidth(float width)
    {
        setSize2D(width, size.y);
    }

    public void setHeight(float height)
    {
        setSize2D(size.x, height);
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

    public Vector3f getOrigin()
    {
        return new Vector3f(origin.x, origin.y, origin.z);
    }

    public Vector2f getOrigin2D()
    {
        return new Vector2f(origin.x, origin.y);
    }

    public void setOrigin(Vector3f origin)
    {
        this.origin = new Vector3f(origin);
    }

    public void setOrigin(Vector2f origin)
    {
        this.origin = new Vector3f(origin.x, origin.y, 0);
    }

    public void setOrigin(float x, float y)
    {
        this.origin.x = x;
        this.origin.y = y;
        this.origin.z = 0;
    }

    public void setTopLeftOrigin()
    {
        setOrigin(0, 0);
    }

    public void setTopRightOrigin()
    {
        setOrigin(size.x, 0);
    }

    public void setBottomLeftOrigin()
    {
        setOrigin(0, size.y);
    }

    public void setBottomRightOrigin()
    {
        setOrigin(size.x, size.y);
    }

    public void setCenterOrigin()
    {
        setOrigin(size.x / 2, size.y / 2);
    }


    public void setOriginPosition(Vector2f origin, Vector2f position)
    {
        setPosition2D(new Vector2f(position).add(getOrigin2D().sub(origin)));
    }

    public Vector2f getOriginPosition(Vector2f origin)
    {
        return new Vector2f(getPosition2D()).sub(getOrigin2D().sub(origin));
    }

    public Vector2f getTopLeftPosition()
    {
        return getOriginPosition(new Vector2f(0, 0));
    }

    public Vector2f getCenterPosition()
    {
        return getOriginPosition(getSize2D().div(2f));
    }

    public Vector2f getTopRightPosition()
    {
        return getOriginPosition(new Vector2f(getWidth(), 0));
    }

    public Vector2f getBottomLeftPosition()
    {
        return getOriginPosition(new Vector2f(0, getHeight()));
    }

    public Vector2f getBottomRightPosition()
    {
        return getOriginPosition(new Vector2f(getWidth(), getHeight()));
    }

    public void setTopLeftPosition(Vector2f pos)
    {
        setOriginPosition(new Vector2f(0, 0), pos);
    }

    public void setCenterPosition(Vector2f pos)
    {
        setOriginPosition(getSize2D().div(2f), pos);
    }

    public void setTopRightPosition(Vector2f pos)
    {
        setOriginPosition(new Vector2f(getWidth(), 0), pos);
    }

    public void setBottomLeftPosition(Vector2f pos)
    {
        setOriginPosition(new Vector2f(0, getHeight()), pos);
    }

    public void setBottomRightPosition(Vector2f pos)
    {
        setOriginPosition(new Vector2f(getWidth(), getHeight()), pos);
    }

    public void center()
    {
        Vector2f pos = Application.getApplication().getScene().getCamera().getPosition().add(Window.getWindow().getCenter());
        setPosition2D(pos);
    }

    public Vector3f getPosition()
    {
        return new Vector3f(position.x, position.y, position.z);
    }

    public Vector3f getSize()
    {
        return new Vector3f(size.x, size.y, size.z);
    }

    public Vector3f getRotation()
    {
        return new Vector3f(rotation.x, rotation.y, rotation.z);
    }

    public Vector3f getPositionRef()
    {
        return position;
    }

    public Vector3f getSizeRef()
    {
        return size;
    }

    public Vector3f getRotationRef()
    {
        return rotation;
    }

    public float getAngle(Vector2f target)
    {
        return (float) Math.toDegrees(Math.atan2(-(position.y - target.y), -(position.x - target.x)));
    }

    public Vector3f getOriginRef()
    {
        return origin;
    }

    public float getAngle()
    {
        return rotation.z;
    }

    public void setAngle(float angle)
    {
        rotation.z = angle;
    }
    
    public float getDistance(Vector2f position)
    {
        return getCenterPosition().distance(position);
    }
}
