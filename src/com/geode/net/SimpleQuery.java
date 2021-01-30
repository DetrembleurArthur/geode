package com.geode.net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class SimpleQuery implements Serializable
{
    protected String type;
    protected ArrayList<Serializable> args;

    public SimpleQuery()
    {
        this("q");
    }

    public SimpleQuery(String type)
    {
        this(type, new ArrayList<>());
    }

    public SimpleQuery(String type, Serializable ... args)
    {
        this.args = (ArrayList<Serializable>) Arrays.asList(args);
        setType(type);
    }

    public SimpleQuery(String type, ArrayList<Serializable> args)
    {
        this.args = args;
        setType(type);
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type.toLowerCase();
    }

    public ArrayList<Serializable> getArgs()
    {
        return args;
    }

    public Serializable[] getArgsArray()
    {
        return args.toArray(new Serializable[0]);
    }

    public void setArgs(ArrayList<Serializable> args)
    {
        this.args = args;
    }

    public SimpleQuery pack(Serializable ... args)
    {
        this.args.addAll(Arrays.asList(args));
        return this;
    }
    @Override
    public String toString()
    {
        return "Q [" + type + "::" + args + "]";
    }
}
