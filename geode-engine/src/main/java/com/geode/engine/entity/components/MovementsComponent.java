package com.geode.engine.entity.components;

import com.geode.engine.core.Time;
import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.Transform;
import com.geode.engine.entity.Updatable;
import com.geode.engine.utils.MathUtils;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class MovementsComponent extends Component
{
    private final Transform transform;

    private boolean useProperties = false;
    @Getter @Setter
    private Vector2f speedMax = new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
    private final Vector2f speed = new Vector2f();
    private Vector2f acceleration = new Vector2f();

    public MovementsComponent(GameObject parent, Integer priority)
    {
        super(parent, priority);
        transform = parent.getTransform();
    }

    @Override
    public void update()
    {

        if (useProperties)
        {
            getParent().properties_c().speed().modify(value -> value.add(getAcceleration().mul(Time.getDt())));
            moveProp(speed);
        } else
        {
            speed.add(getAcceleration().mul(Time.getDt()));
            move(speed);
        }
    }

    public boolean isUseProperties()
    {
        return useProperties;
    }

    public void setUseProperties(boolean useProperties)
    {
        this.useProperties = useProperties;
    }

    public void setAcceleration(Vector2f acceleration)
    {
        this.acceleration = acceleration;
    }

    public Vector2f getAcceleration()
    {
        return new Vector2f(acceleration);
    }

    public Vector2f getAccelerationRef()
    {
        return acceleration;
    }

    public void setSpeed(Vector2f speed)
    {
        setSpeedX(speed.x);
        setSpeedY(speed.y);
    }

    public Vector2f getSpeed()
    {
        return new Vector2f(speed);
    }

    public Vector2f getSpeedRef()
    {
        return speed;
    }

    public void setSpeedX(float x)
    {
        if(x <= speedMax.x)
            speed.x = x;
    }

    public float getSpeedX()
    {
        return speed.x;
    }

    public void setSpeedY(float y)
    {
        if(speed.y <= speedMax.y)
            speed.y = y;
    }

    public float getSpeedY()
    {
        return speed.y;
    }

    public void moveX(float translationX)
    {
        transform.addX(translationX * Time.getDt());
    }

    public void moveY(float translationY)
    {
        transform.addY(translationY * Time.getDt());
    }

    public void moveXProp(float translationX)
    {
        getParent().properties_c().x().modify(x -> x + translationX * Time.getDt());
    }

    public void moveYProp(float translationY)
    {
        getParent().properties_c().y().modify(y -> y + translationY * Time.getDt());
    }

    public void move(float translationX, float translationY)
    {
        float dt = Time.getDt();
        transform.addPosition2D(translationX * dt, translationY * dt);
    }

    public void moveProp(float translationX, float translationY)
    {
        float dt = Time.getDt();
        var properties = getParent().properties_c();
        properties.position2D().modify(pos -> pos.add(translationX * dt, translationY * dt));
    }

    public void move(Vector2f translation)
    {
        move(translation.x, translation.y);
    }

    public void moveProp(Vector2f translation)
    {
        moveProp(translation.x, translation.y);
    }

    public Vector2f getVectorComponent(Vector2f pos)
    {
        Vector2f position = transform.getPosition2D();
        if (pos.x == position.x && pos.y == position.y) return new Vector2f();
        return pos.sub(position).normalize();
    }

    public Vector2f getVectorComponent(float x, float y)
    {
        return getVectorComponent(new Vector2f(x, y));
    }

    public Vector2f getVectorComponent(Vector2f pos, Vector2f speed)
    {
        return getVectorComponent(pos).mul(speed);
    }

    public Vector2f getVectorComponent(float x, float y, float sx, float sy)
    {
        return getVectorComponent(new Vector2f(x, y), new Vector2f(sx, sy));
    }

    public Vector2f getVectorComponent(float x, float y, float s)
    {
        return getVectorComponent(x, y, s, s);
    }

    public Vector2f getVectorComponent(Vector2f pos, float s)
    {
        return getVectorComponent(pos, new Vector2f(s, s));
    }

    public void moveToward(Vector2f pos, Vector2f speed)
    {
        move(getVectorComponent(pos, speed));
    }

    public void moveToward(float x, float y, float sx, float sy)
    {
        move(getVectorComponent(x, y, sx, sy));
    }

    public void moveToward(Vector2f pos, float speed)
    {
        move(getVectorComponent(pos, speed));
    }

    public void moveToward(float x, float y, float speed)
    {
        move(getVectorComponent(x, y, speed));
    }

    public void moveTowardProp(Vector2f pos, Vector2f speed)
    {
        moveProp(getVectorComponent(pos, speed));
    }

    public void moveTowardProp(float x, float y, float sx, float sy)
    {
        moveProp(getVectorComponent(x, y, sx, sy));
    }

    public void moveTowardProp(Vector2f pos, float speed)
    {
        moveProp(getVectorComponent(pos, speed));
    }

    public void moveTowardProp(float x, float y, float speed)
    {
        moveProp(getVectorComponent(x, y, speed));
    }

    public void rotate(float angle)
    {
        transform.getRotationRef().z += angle * Time.getDt();
    }

    public void rotateProp(float angle)
    {
        getParent().properties_c().angle().modify(value -> value + angle * Time.getDt());
    }

    public void rotate(Vector3f rotation)
    {
        transform.getRotationRef().add(new Vector3f(rotation).mul(Time.getDt()));
    }

    public void rotateAround(Vector2f aroundPoint, float angle)
    {
        transform.setPosition2D(MathUtils.rotateAround(
                transform.getPosition2D(),
                aroundPoint,
                angle * Time.getDt()));
    }

    public void rotateAround(Vector2f aroundPoint, Vector2f angles)
    {
        transform.setPosition2D(MathUtils.rotateAround(
                transform.getPosition2D(),
                aroundPoint,
                new Vector2f(angles).mul(Time.getDt())));
    }

    public void rotateAroundProp(Vector2f aroundPoint, float angle)
    {
        getParent().properties_c().position2D().set(MathUtils.rotateAround(
                transform.getPosition2D(),
                aroundPoint,
                angle * Time.getDt()));
    }

    public void rotateAroundProp(Vector2f aroundPoint, Vector2f angles)
    {
        getParent().properties_c().position2D().set(MathUtils.rotateAround(
                transform.getPosition2D(),
                aroundPoint,
                new Vector2f(angles).mul(Time.getDt())));
    }


    public float getTowardRotationComponent(Vector2f target)
    {
        float r1 = transform.getAngle(target);
        float r2 = transform.getAngle();
        float rt1 = r1 - r2;
        float rt2 = 360 - Math.abs(rt1);
        return Float.compare(Math.max(rt2, Math.abs(rt1)), rt2) == 0 ? rt1 : -rt2;
    }

    public float getTowardRotationComponent(Vector2f target, float speed)
    {
        return getTowardRotationComponent(target) * speed;
    }

    public void rotateToward(Vector2f target, float speed)
    {
        rotate(getTowardRotationComponent(target, speed * Time.getDt()));
    }

    public void rotateToward(float x, float y, float speed)
    {
        rotateToward(new Vector2f(x, y), speed);
    }

    public void rotateTowardProp(Vector2f target, float speed)
    {
        rotateProp(getTowardRotationComponent(target, speed * Time.getDt()));
    }

    public void rotateTowardProp(float x, float y, float speed)
    {
        rotateTowardProp(new Vector2f(x, y), speed);
    }

    public void placeAround(Vector2f position, float distance, Vector2f radComponent)
    {
        transform.setPosition2D(new Vector2f(position).add(new Vector2f(distance).mul(radComponent)));
    }

    public void placeAround(Vector2f position, float distance, float angleDegree)
    {
        placeAround(position, distance, MathUtils.degreeAngleToRadianVector(angleDegree));
    }

    public void placeAroundProp(Vector2f position, float distance, Vector2f radComponent)
    {
        getParent().properties_c().position2D().set(new Vector2f(position).add(new Vector2f(distance).mul(radComponent)));
    }

    public void placeAroundProp(Vector2f position, float distance, float angleDegree)
    {
        placeAroundProp(position, distance, MathUtils.degreeAngleToRadianVector(angleDegree));
    }

    public void lookAt(Vector2f target)
    {
        transform.getRotationRef().z = transform.getAngle(target);
    }

    public void lookAt(float targetX, float targetY)
    {
        lookAt(new Vector2f(targetX, targetY));
    }

    public void lookAtProp(Vector2f target)
    {
        getParent().properties_c().angle().set(transform.getAngle(target));
    }

    public void lookAtProp(float targetX, float targetY)
    {
        lookAtProp(new Vector2f(targetX, targetY));
    }
}
