package com.geode.net;

import java.io.Serializable;
import java.util.ArrayList;

public class Args extends ArrayList<Serializable>
{
	private int it = 0;

	public <T> T getc(int i)
	{
		return (T) get(i);
	}
	
	public <T> T get()
	{
		if(it < size())
			return getc(it++);
		return null;
	}

	public Args castToPrimitive(Class<?>[] types)
	{
		Args args = new Args();
		int i = 0;
		for(Class<?> type : types)
		{
			if(type.equals(Integer.class))
				args.add(Integer.parseInt((String) get(i)));
			else if(type.equals(Float.class))
				args.add(Float.parseFloat((String) get(i)));
			else if(type.equals(Double.class))
				args.add(Double.parseDouble((String) get(i)));
			else if(type.equals(Boolean.class))
				args.add(Boolean.parseBoolean((String) get(i)));
			else if(type.equals(Character.class))
				args.add(((String) get(i)).charAt(0));
			else
				args.add(get(i));
			i++;
		}
		return args;
	}

	public void reset()
	{
		setIt(0);
	}
	
	public int getIt()
	{
		return it;
	}

	public void setIt(int it)
	{
		this.it = it;
	}
}
