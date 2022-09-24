package com.geode.engine.entity.components.event;


import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;

public abstract class MouseButtonEvent extends MouseEvent
{
	protected int buttonId;

	public MouseButtonEvent(GameObject relativeObject, int buttonId, Camera camera2D)
	{
		super(relativeObject, camera2D);
		this.buttonId = buttonId;
	}

	public int getButtonId()
	{
		return buttonId;
	}
}
