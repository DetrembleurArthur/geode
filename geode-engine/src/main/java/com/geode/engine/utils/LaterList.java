package com.geode.engine.utils;

import java.util.ArrayList;
import java.util.Collection;

public class LaterList<T> extends ArrayList<T>
{
	private final ArrayList<T> addLaterList;
	private final ArrayList<T> removeLaterList;
	private boolean mustBeUpdated = false;

	public LaterList()
	{
		super();
		addLaterList = new ArrayList<>();
		removeLaterList = new ArrayList<>();
	}

	public void addLater(T dynamic)
	{
		addLaterList.add(dynamic);
		mustBeUpdated = true;
	}

	public void removeLater(T dynamic)
	{
		removeLaterList.add(dynamic);
		mustBeUpdated = true;
	}

	public void addLater(Collection<? extends T> dynamic)
	{
		addLaterList.addAll(dynamic);
		mustBeUpdated = true;
	}

	public void removeLater(Collection<? extends T> dynamic)
	{
		removeLaterList.addAll(dynamic);
		mustBeUpdated = true;
	}

	public void sync()
	{
		if(mustBeUpdated)
		{
			removeAll(removeLaterList);
			addAll(addLaterList);
			removeLaterList.clear();
			addLaterList.clear();
			mustBeUpdated = false;
		}
	}

	public ArrayList<T> getAddLaterList()
	{
		return addLaterList;
	}

	public boolean isMustBeUpdated()
	{
		return mustBeUpdated;
	}
}
