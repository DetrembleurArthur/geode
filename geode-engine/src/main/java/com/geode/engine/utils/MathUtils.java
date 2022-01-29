package com.geode.engine.utils;

import com.geode.engine.graphics.Camera;
import com.geode.engine.system.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class MathUtils
{
    public static Vector2f screenCoordsToWorldCoords(Vector2f screen, Camera camera)
    {
        screen.sub(Window.getWindow().getViewportPos());
        Vector2f norm = screen.div(Window.getWindow().getViewportSize()).mul(2).sub(1, 1);
        //inversion of Y coords
        //norm.y = -norm.y;
        Vector4f world4f = new Vector4f(norm.x, -norm.y, 0, 1).mul(camera.getInvProjection()).mul(camera.getInvView());
        return new Vector2f(world4f.x, world4f.y);
    }
}
