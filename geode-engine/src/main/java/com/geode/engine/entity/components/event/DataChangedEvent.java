package com.geode.engine.entity.components.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DataChangedEvent extends RelativeEvent
{
	private final Method getter;
	private Object oldValue;

	public DataChangedEvent(Object object, Method method)
	{
		super(object);
		this.getter = method;
		oldValue = invoke();
	}

	@Override
	boolean isAppend()
	{
		Object obj = invoke();
		if(!obj.equals(oldValue))
		{
			oldValue = obj;
			return true;
		}
		return false;
	}

	public Object invoke()
	{
		try
		{
			return getter.invoke(object);
		} catch (IllegalAccessException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
