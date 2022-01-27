package com.geode.engine.entity;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
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
}
