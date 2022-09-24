package com.geode.engine.entity.components.event;

import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;
import org.lwjgl.glfw.GLFW;

public class MouseRightButtonClickEvent extends MouseButtonClickEvent
{
    public MouseRightButtonClickEvent(GameObject relativeObject, boolean repeated, Camera camera2D)
    {
        super(relativeObject, GLFW.GLFW_MOUSE_BUTTON_RIGHT, repeated, camera2D);
    }
}
