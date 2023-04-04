package com.geode.engine.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

public class LaterList<T> extends ArrayList<T>
{
	private final ArrayList<T> addLaterList;
	private final ArrayList<T> removeLaterList;
	private boolean mustBeUpdated = false;
	@Getter
	private boolean hadAdditions = false;
	@Getter
	private boolean hadDeletions = false;

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
		hadAdditions = true;
	}

	public void removeLater(T dynamic)
	{
		removeLaterList.add(dynamic);
		mustBeUpdated = true;
		hadDeletions = true;
	}

	public void addLater(Collection<? extends T> dynamic)
	{
		addLaterList.addAll(dynamic);
		mustBeUpdated = true;
		hadAdditions = true;
	}

	public void removeLater(Collection<? extends T> dynamic)
	{
		removeLaterList.addAll(dynamic);
		mustBeUpdated = true;
		hadDeletions = true;
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
			hadDeletions = false;
			hadAdditions = false;
		}
	}

	public ArrayList<T> getAddLaterList()
	{
		return addLaterList;
	}

	public boolean mustBeUpdated()
	{
		return mustBeUpdated;
	}
}
