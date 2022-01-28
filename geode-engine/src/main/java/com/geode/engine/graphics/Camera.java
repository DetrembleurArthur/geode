package com.geode.engine.graphics;

import com.geode.engine.system.Window;
import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera
{
    @Getter
    private final Matrix4f projection;

    @Getter
    private final Matrix4f view;

    @Getter
    private Vector2f position;

    @Getter
    private final Vector4f orthoSettings;

    @Getter
    private final Vector3f zoom;

    public Camera(float left, float right, float bottom, float up)
    {
        position = new Vector2f();
        projection = new Matrix4f();
        view = new Matrix4f();
        this.orthoSettings = new Vector4f(left, right, bottom, up);
        zoom = new Vector3f(1,1,1);
        updateProjection();
    }

    public Camera()
    {
        this(0, Window.getWindow().getSize().x, 0, Window.getWindow().getSize().y);
    }

    public void updateProjection()
    {
        projection.identity().ortho(orthoSettings.x, orthoSettings.y, orthoSettings.z, orthoSettings.w, 0f, 100f);
    }

    public Matrix4f updateViewMatrix()
    {
        return view.identity().lookAt(
                        new Vector3f(position.x, position.y, 20f),
                        new Vector3f(0f, 0f, -1f).add(position.x, position.y, 0f),
                        new Vector3f(0f, 1f, 0f))
                .scaleAround(zoom.x, zoom.y, zoom.z, position.x + getRight() / 2, position.y + getUp() / 2, 0);
    }

    public void focus(Vector2f position)
    {
        this.position = new Vector2f(position).sub(getRight() / 2, getUp() / 2);
    }

    public Matrix4f getInvProjection()
    {
        return new Matrix4f(projection).invert();
    }

    public Matrix4f getInvView()
    {
        return new Matrix4f(view).invert();
    }

    public float getLeft()
    {
        return orthoSettings.x;
    }

    public float getRight()
    {
        return orthoSettings.y;
    }

    public float getBottom()
    {
        return orthoSettings.z;
    }

    public float getUp()
    {
        return orthoSettings.w;
    }
}