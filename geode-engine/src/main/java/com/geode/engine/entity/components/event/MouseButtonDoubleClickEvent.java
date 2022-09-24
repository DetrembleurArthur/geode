package com.geode.engine.entity.components.event;


import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;
import com.geode.engine.timer.StaticTimer;

public class MouseButtonDoubleClickEvent extends MouseLeftButtonClickEvent
{
	private StaticTimer timer;

	public MouseButtonDoubleClickEvent(GameObject relativeObject, Camera camera2D)
	{
		super(relativeObject, false, camera2D);
		timer = new StaticTimer(200f);
	}

	@Override
	boolean isAppend()
	{
		boolean append = super.isAppend();
		if(!timer.isRunning())
		{
			if(append)
			{
				timer.activate();
			}
		}
		else
		{
			if(append)
			{
				if(!timer.isFinished())
				{
					timer.cancel();
					return true;
				}
				else
				{
					timer.cancel();
				}
			}
			else
			{
				if(timer.isFinished())
				{
					timer.cancel();
				}
			}
		}
		return false;
	}
}
