package com.geode.engine.entity.components.event;

import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;

public abstract class MouseEvent extends RelativeEvent
{
	protected final Camera camera;

	public MouseEvent(GameObject relativeObject, Camera camera)
	{
		super(relativeObject);
		this.camera = camera;
		this.object = relativeObject;
	}

	public Camera getCamera()
	{
		return camera;
	}
}
