package com.geode.engine.entity.components.event;


import com.geode.engine.core.MouseManager;
import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;

public class MouseHoveringEvent extends MouseEvent
{
	public MouseHoveringEvent(GameObject relativeObject, Camera camera2D)
	{
		super(relativeObject, camera2D);
	}

	@Override
	boolean isAppend()
	{
		return ((GameObject)object).collision_c().contains(MouseManager.getMousePositionf(camera));
	}
}
