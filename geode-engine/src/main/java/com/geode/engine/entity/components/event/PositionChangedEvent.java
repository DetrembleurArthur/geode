package com.geode.engine.entity.components.event;


import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.Transform;
import com.geode.engine.utils.Introspection;

public class PositionChangedEvent extends DataChangedEvent
{
	public PositionChangedEvent(Transform object)
	{
		super(object, Introspection.getMethod(Transform.class, "getPosition2D"));
	}
}
