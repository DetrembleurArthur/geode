package com.geode.engine.utils;

import com.geode.engine.graphics.Camera;
import com.geode.engine.core.Window;
import org.joml.Matrix2f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class MathUtils
{
    public static Vector2f screenToWorld(Vector2f screen, Camera camera)
    {
        screen.sub(Window.getWindow().getViewportPos());
        Vector2f norm = screen.div(Window.getWindow().getViewportSize()).mul(2).sub(1, 1);
        Vector4f world4f = new Vector4f(norm.x, -norm.y, 0, 1).mul(camera.getInvProjection()).mul(camera.getInvView());
        return new Vector2f(world4f.x, world4f.y);
    }

    public static Vector2f rotateAround(Vector2f rotable, Vector2f rotPoint, Vector2f anglesDegree)
    {
        Vector2f anglesRad = new Vector2f(
            (float) Math.toRadians(anglesDegree.x),
            (float) Math.toRadians(anglesDegree.y)
        );
        rotable.sub(rotPoint);
        return new Vector2f(
            (float)(rotable.x * Math.cos(anglesRad.x) - rotable.y  * Math.sin(anglesRad.y)),
            (float)(rotable.x * Math.sin(anglesRad.x) + rotable.y  * Math.cos(anglesRad.y))
        ).add(rotPoint);
    }

    public static Vector2f rotateAround(Vector2f rotable, Vector2f rotPoint, float angleDegree)
    {
        return rotateAround(rotable, rotPoint, new Vector2f(angleDegree, angleDegree));
    }

    public static Vector2f degreeAngleToRadianVector(float angle)
    {
        angle = (float) Math.toRadians(angle);
        return new Vector2f((float)Math.cos(angle), (float)Math.sin(angle));
    }
}
