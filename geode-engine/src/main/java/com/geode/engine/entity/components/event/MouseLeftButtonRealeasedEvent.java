package com.geode.engine.entity.components.event;

import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseLeftButtonRealeasedEvent extends MouseButtonReleasedEvent
{
    public MouseLeftButtonRealeasedEvent(GameObject relativeObject, Camera camera2D)
    {
        super(relativeObject, GLFW_MOUSE_BUTTON_LEFT, camera2D);
    }
}
