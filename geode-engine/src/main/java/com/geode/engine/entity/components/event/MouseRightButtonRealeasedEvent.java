package com.geode.engine.entity.components.event;

import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;
import org.lwjgl.glfw.GLFW;

public class MouseRightButtonRealeasedEvent extends MouseButtonReleasedEvent
{
    public MouseRightButtonRealeasedEvent(GameObject relativeObject, Camera camera2D)
    {
        super(relativeObject, GLFW.GLFW_MOUSE_BUTTON_RIGHT, camera2D);
    }
}
