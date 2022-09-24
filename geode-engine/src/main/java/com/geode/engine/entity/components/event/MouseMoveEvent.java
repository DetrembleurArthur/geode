package com.geode.engine.entity.components.event;


import com.geode.engine.core.MouseManager;
import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;
import org.joml.Vector2f;

public class MouseMoveEvent extends MouseEvent
{
	private Vector2f lastMousePosition;

	public MouseMoveEvent(GameObject relativeObject, Camera camera2D)
	{
		super(relativeObject, camera2D);
		lastMousePosition = new Vector2f();
	}

	@Override
	boolean isAppend()
	{
		Vector2f newMousePosition = MouseManager.getMousePositionf(camera);
		if(!newMousePosition.equals(lastMousePosition) && ((GameObject)object).collision_c().contains(newMousePosition))
		{
			lastMousePosition = newMousePosition;
			return true;
		}
		return false;
	}

	public Vector2f getLastMousePosition()
	{
		return lastMousePosition;
	}
}
