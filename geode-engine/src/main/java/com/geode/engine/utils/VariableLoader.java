package com.geode.engine.utils;

import java.util.Properties;

public class VariableLoader
{
	private Properties variables;

	public VariableLoader()
	{
		variables = new Properties();
	}

	public void load(String expr)
	{
		var splitted = expr.split(" ");
		for(var s : splitted)
		{
			var var = s.split("=");
			if(var.length == 2)
			{
				variables.setProperty(var[0], var[1]);
			}
		}
	}

	public int getInt(String name)
	{
		return Integer.parseInt(variables.getProperty(name));
	}

	public float getFloat(String name)
	{
		return Float.parseFloat(variables.getProperty(name));
	}

	public String getString(String name)
	{
		return variables.getProperty(name);
	}

	public boolean getBool(String name)
	{
		return getInt(name) == 1;
	}

	public float[] getFloatA(String name, int n)
	{
		String buf = variables.getProperty(name);
		float[] f = new float[n];
		var splitted = buf.split(",");
		if(splitted.length == n)
		{
			int i = 0;
			for(var s : splitted)
			{
				f[i++] = Float.parseFloat(s);
			}
		}
		return f;
	}

	public void clean()
	{
		variables.clear();
	}
}
