package com.geode.engine.tweening;

public class TweenAction implements Runnable
{
	protected float startValue;
	protected float endValue;
	protected float currentPercent;
	private TweenFunction func;
	protected TweenSetter binder;
	protected TweenGetter stepper;

	public TweenAction(float startValue, float endValue, TweenFunction func, TweenSetter binder, TweenGetter stepper)
	{
		this.startValue = startValue;
		this.endValue = endValue;
		this.func = func;
		this.binder = binder;
		this.stepper = stepper;
		this.currentPercent = 0f;
	}

	public TweenFunction getFunc()
	{
		return func;
	}

	public void setFunc(TweenFunction func)
	{
		this.func = func;
	}

	public float getStartValue()
	{
		return startValue;
	}

	public void setStartValue(float startValue)
	{
		this.startValue = startValue;
	}

	public float getEndValue()
	{
		return endValue;
	}

	public void setEndValue(float endValue)
	{
		this.endValue = endValue;
	}

	public float getCurrentPercent()
	{
		return currentPercent;
	}

	public void setCurrentPercent(float currentPercent)
	{
		if(currentPercent < 0) currentPercent = 0;
		else if(currentPercent > 1) currentPercent = 1;
		this.currentPercent = currentPercent;
	}

	public void setCurrentValue(float value)
	{
		float percent = (value-startValue) / (endValue - startValue);
		setCurrentPercent(percent);
	}

	protected float action()
	{
		return startValue + (endValue - startValue) * func.f(currentPercent);
	}

	public static float get(TweenFunction f, float beg, float end, float value)
	{
		return beg + (end - beg) * f.f((value-beg) / (end - beg));
	}

	public static float getByProgress(TweenFunction f, float beg, float end, float perc)
	{
		return beg + (end - beg) * f.f(perc);
	}

	public static float getProgress(TweenFunction f, float beg, float end, float value)
	{
		return f.f((value-beg) / (end - beg));
	}

	public boolean isFinished()
	{
		return currentPercent >= 1f;
	}

	public void swap()
	{
		float tmp = endValue;
		endValue = startValue;
		startValue = tmp;
	}

	public TweenGetter getStepper()
	{
		return stepper;
	}

	public void setStepper(TweenGetter stepper)
	{
		this.stepper = stepper;
	}

	public TweenSetter getBinder()
	{
		return binder;
	}

	public void setBinder(TweenSetter binder)
	{
		this.binder = binder;
	}

	@Override
	public void run()
	{
		setCurrentPercent(stepper.get());
		binder.set(action());
	}
}
