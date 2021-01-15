package com.geode.net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Query implements Serializable
{
    public enum Mode
    {
        NORMAL
    }
    
    public static final Object SUCCESS = new Object();
    public static final Object FAILED = new Object();

    private Mode mode;
    private String type;
    private Args args;

    public static Query simple(String buffer)
    {
    	return new Query(buffer);
    }
    
    public static Query success(String buffer)
    {
    	return new Query(buffer).success();
    }
    
    public static Query failed(String buffer)
    {
    	return new Query(buffer).failed();
    }
    
    public Query()
    {
        this("undefined");
    }

    public Query(String type)
    {
        this(type, Mode.NORMAL);
    }

    public Query(String type, Mode mode)
    {
        this(type, mode, new Serializable[0]);
    }

    public Query(String type, Mode mode, Serializable ... serializables)
    {
        args = new Args();
        this.type = type;
        this.mode = mode;
        args.addAll(Arrays.asList(serializables));
    }

    public Query pack(Serializable arg)
    {
        args.add(arg);
        return this;
    }
    
    public Query packfirst(Serializable arg)
    {
        args.add(0, arg);
        return this;
    }

    public Query packall(ArrayList<Serializable> objects)
    {
        args.addAll(objects);
        return this;
    }

    public boolean is(String type)
    {
        return getType().toLowerCase().equals(type.toLowerCase());
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Args getArgs()
    {
        return args;
    }

    public void setArgs(Args args)
    {
        this.args = args;
    }
    
    public Query success()
    {
    	type += "_success";
    	return this;
    }
    
    public Query failed()
    {
    	type += "_failed";
    	return this;
    }

    Query mode(Mode mode)
    {
        this.mode = mode;
        return this;
    }

    public Mode getMode()
    {
        return mode;
    }

    @Override
    public String toString()
    {
        StringBuilder buffer = new StringBuilder(type);
        buffer.append("|").append(mode);
        for(Object obj : args)
            buffer.append("|").append(obj.toString());
        return buffer.toString();
    }
}
