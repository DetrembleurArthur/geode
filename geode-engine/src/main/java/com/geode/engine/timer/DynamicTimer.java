package com.geode.engine.timer;


public abstract class DynamicTimer extends StaticTimer implements Runnable
{
	public static final int INFINITE = -1;
	protected int maxPeriod;
	private int periodCounter;
	protected boolean continuousTrigger = false;
	protected Runnable action;
	protected boolean actionRunned = false;

	public DynamicTimer(float maxDelay, int maxPeriod, Runnable action)
	{
		super(maxDelay);
		this.maxPeriod = maxPeriod;
		this.action = action;
	}

	public boolean isActionRunned()
	{
		return actionRunned;
	}

	public void setActionRunned(boolean actionRunned)
	{
		this.actionRunned = actionRunned;
	}

	@Override
	public void activate()
	{
		super.activate();
		periodCounter = 0;
		actionRunned = false;
	}

	@Override
	public void cancel()
	{
		super.cancel();
		periodCounter = 0;
		actionRunned = false;
	}

	public void runAction()
	{
		if(!isActionRunned())
		{
			action.run();
			setActionRunned(true);
		}
	}

	public void runContinuousAction()
	{
		action.run();
	}

	public void resetTime()
	{
		super.activate();
	}

	protected void tryNextPeriod()
	{
		if(periodCounter + 1 <= maxPeriod || maxPeriod == INFINITE)
		{
			periodCounter++;
			resetTime();
			if(!isContinuousTrigger())
				actionRunned = false;
		}
		else
		{
			cancel();
		}
	}

	public Runnable getAction()
	{
		return action;
	}

	public void setAction(Runnable action)
	{
		this.action = action;
	}

	public int getMaxPeriod()
	{
		return maxPeriod;
	}

	public void setMaxPeriod(int maxPeriod)
	{
		this.maxPeriod = maxPeriod;
	}

	public int getPeriodCounter()
	{
		return periodCounter;
	}

	public void setPeriodCounter(int periodCounter)
	{
		this.periodCounter = periodCounter;
	}

	public boolean isContinuousTrigger()
	{
		return continuousTrigger;
	}

	public void setContinuousTrigger(boolean continuousTrigger)
	{
		this.continuousTrigger = continuousTrigger;
	}

	protected void test()
	{
		if(isFinished())
		{
			if(!isContinuousTrigger())
			{
				runAction();
			}
			tryNextPeriod();
		}
		else
		{
			if(isContinuousTrigger())
			{
				runContinuousAction();
			}
		}
	}
}
