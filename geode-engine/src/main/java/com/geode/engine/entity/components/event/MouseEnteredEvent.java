package com.geode.engine.entity.components.event;


import com.geode.engine.core.MouseManager;
import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;

public class MouseEnteredEvent extends MouseEvent
{
	protected boolean entered;

	public MouseEnteredEvent(GameObject relativeObject, Camera camera2D)
	{
		super(relativeObject, camera2D);
		entered = false;
	}

	@Override
	boolean isAppend()
	{
		boolean intersects = ((GameObject)object).collision_c().contains(MouseManager.getMousePositionf(camera));
		if(intersects)
		{
			if(!entered)
			{
				entered = true;
				return true;
			}
		}
		else
		{
			if(entered)
			{
				entered = false;
			}
		}
		return false;
	}

	public boolean isEntered()
	{
		return entered;
	}
}
