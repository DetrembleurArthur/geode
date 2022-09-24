package com.geode.engine.entity.components.event;

import com.geode.engine.entity.GameObject;
import com.geode.engine.utils.Introspection;

public class FillColorChangedEvent extends DataChangedEvent
{
	public FillColorChangedEvent(GameObject object)
	{
		super(object, Introspection.getMethod(GameObject.class, "getColor"));
	}
}
