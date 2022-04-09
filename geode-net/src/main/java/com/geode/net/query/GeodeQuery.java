package com.geode.net.query;

import java.io.Serializable;
import java.util.ArrayList;

public class GeodeQuery implements Serializable
{
    private long type;
    private ArrayList<Serializable> args;

    public GeodeQuery()
    {

    }

    public long getType()
    {
        return type;
    }

    public void setType(long type)
    {
        this.type = type;
    }

    public ArrayList<Serializable> getArgs()
    {
        return args;
    }

    public void setArgs(ArrayList<Serializable> args)
    {
        this.args = args;
    }
}
