package com.geode.engine.entity.components.event;


import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseLeftButtonClickEvent extends MouseButtonClickEvent
{
	public MouseLeftButtonClickEvent(GameObject relativeObject, boolean repeated, Camera camera2D)
	{
		super(relativeObject, GLFW_MOUSE_BUTTON_LEFT, repeated, camera2D);
	}
}
