package com.geode.engine.entity.components.event;


import java.util.ArrayList;

public abstract class Event implements Comparable<Event>
{
	private ArrayList<ActionEvent> actions;
	private int priority;
	public static final int LOWER_PRIORITY = Integer.MIN_VALUE;
	public static final int HIGHER_PRIORITY = Integer.MAX_VALUE;

	public Event()
	{
		actions = new ArrayList<>();
		priority = Integer.MIN_VALUE;
	}

	abstract boolean isAppend();
	void run(EventComponent sender)
	{
		for(ActionEvent action : actions)
		{
			action.action(sender);
		}
	}

	public Event addActionEvent(ActionEvent action)
	{
		actions.add(action);
		return this;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	@Override
	public int compareTo(Event o)
	{
		return Integer.compare(priority, o.priority);
	}
}
