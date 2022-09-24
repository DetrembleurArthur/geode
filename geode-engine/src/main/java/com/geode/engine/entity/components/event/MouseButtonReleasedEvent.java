package com.geode.engine.entity.components.event;


import com.geode.engine.core.MouseManager;
import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;

public class MouseButtonReleasedEvent extends MouseButtonEvent
{
	private boolean buttonPressed;
	private boolean falseRelease;

	public MouseButtonReleasedEvent(GameObject relativeObject, Camera camera2D)
	{
		this(relativeObject, -1, camera2D);
	}

	public MouseButtonReleasedEvent(GameObject relativeObject, int buttonId, Camera camera2D)
	{
		super(relativeObject, buttonId, camera2D);
		buttonPressed = false;
		falseRelease = false;
	}

	@Override
	boolean isAppend()
	{
		boolean pressed = buttonId == -1 ? MouseManager.get().isButtonPressed() : MouseManager.get().isPressed(buttonId);
		if(falseRelease)
		{
			if(!pressed)
			{
				falseRelease = false;
				return false;
			}
		}
		else if(buttonPressed)
		{
			if(!pressed)
			{
				buttonPressed = false;
				return true;
			}
		}
		else
		{
			if(pressed)
			{
				if(((GameObject)object).collision_c().contains(MouseManager.getMousePositionf(camera)))
				{
					buttonPressed = true;
				}
				else
				{
					falseRelease = true;
				}
			}
		}
		return false;
	}

	public boolean isButtonPressed()
	{
		return buttonPressed;
	}
}
