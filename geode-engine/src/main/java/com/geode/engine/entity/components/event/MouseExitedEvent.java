package com.geode.engine.entity.components.event;


import com.geode.engine.core.MouseManager;
import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;

public class MouseExitedEvent extends MouseEvent
{
	protected boolean exited;
	public MouseExitedEvent(GameObject relativeObject, Camera camera2D)
	{
		super(relativeObject, camera2D);
		exited = true;
	}

	@Override
	boolean isAppend()
	{
		boolean intersects = ((GameObject)object).collision_c().contains(MouseManager.getMousePositionf(camera));
		if(intersects)
		{
			if(exited)
			{
				exited = false;
			}
		}
		else
		{
			if(!exited)
			{
				exited = true;
				return true;
			}
		}
		return false;
	}

	public boolean isExited()
	{
		return exited;
	}
}
