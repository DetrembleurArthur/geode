package com.geode.engine.graphics;

import com.geode.engine.core.MouseManager;
import com.geode.engine.core.Window;
import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Camera
{
    @Getter
    private final Matrix4f projection;

    @Getter
    private final Matrix4f view;

    @Getter
    private Vector2f position;

    @Getter
    private final OrthoSettings ortho;

    @Getter
    private final Vector3f zoom;

    public Camera(float left, float right, float bottom, float up)
    {
        position = new Vector2f();
        projection = new Matrix4f();
        view = new Matrix4f();
        this.ortho = new OrthoSettings(left, right, bottom, up);
        zoom = new Vector3f(1, 1, 1);
        getProjection();
    }

    public Camera()
    {
        this(0, Window.getWindow().getSize().x, Window.getWindow().getSize().y, 0);
    }

    public Matrix4f getProjection()
    {
        projection.identity().ortho(ortho.getLeft(), ortho.getRight(), ortho.getBottom(), ortho.getUp(), 0f, 100f);
        return projection;
    }

    public Matrix4f updateViewMatrix()
    {
        return view.identity().lookAt(
                        new Vector3f(position.x, position.y, 20f),
                        new Vector3f(0f, 0f, -1f).add(position.x, position.y, 0f),
                        new Vector3f(0f, 1f, 0f))
                .scaleAround(zoom.x, zoom.y, zoom.z, position.x + ortho.getRight() / 2, position.y + ortho.getBottom() / 2, 0);
    }

    public void focus(Vector2f position)
    {
        this.position = new Vector2f(position).sub(ortho.getRight() / 2, ortho.getBottom() / 2);
    }

    public Matrix4f getInvProjection()
    {
        return new Matrix4f(projection).invert();
    }

    public Matrix4f getInvView()
    {
        return new Matrix4f(view).invert();
    }

    public void setZoom(float zoom)
    {
        this.zoom.x = zoom;
        this.zoom.y = zoom;
    }

    public Vector2i getMousePosition()
    {
        return MouseManager.getMousePosition(this);
    }

    public Vector2f getMousePositionf()
    {
        return MouseManager.getMousePositionf(this);
    }
}
