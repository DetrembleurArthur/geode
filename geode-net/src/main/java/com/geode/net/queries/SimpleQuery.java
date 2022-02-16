package com.geode.net.queries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import com.geode.net.json.JSONSerializable;

/**
 * The type Simple query.
 */
public abstract class SimpleQuery implements JSONSerializable
{
    /**
     * The Type.
     */
    protected String type;
    /**
     * The Args.
     */
    protected ArrayList<Serializable> args;

    /**
     * Instantiates a new Simple query.
     */
    public SimpleQuery()
    {
        this("q");
    }

    /**
     * Instantiates a new Simple query.
     *
     * @param type the type
     */
    public SimpleQuery(String type)
    {
        this(type, new ArrayList<>());
    }

    /**
     * Instantiates a new Simple query.
     *
     * @param type the type
     * @param args the args
     */
    public SimpleQuery(String type, Serializable... args)
    {
        this.args = (ArrayList<Serializable>) Arrays.asList(args);
        setType(type);
    }

    /**
     * Instantiates a new Simple query.
     *
     * @param type the type
     * @param args the args
     */
    public SimpleQuery(String type, ArrayList<Serializable> args)
    {
        this.args = args;
        setType(type);
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type)
    {
        this.type = type.toLowerCase();
    }

    /**
     * Gets args.
     *
     * @return the args
     */
    public ArrayList<Serializable> getArgs()
    {
        return args;
    }

    /**
     * Sets args.
     *
     * @param args the args
     */
    public void setArgs(ArrayList<Serializable> args)
    {
        this.args = args;
    }

    /**
     * Get args array serializable [ ].
     *
     * @return the serializable [ ]
     */
    public Serializable[] getArgsArray()
    {
        return args.toArray(new Serializable[0]);
    }

    /**
     * Pack simple query.
     *
     * @param args the args
     * @return the simple query
     */
    public SimpleQuery pack(Serializable... args)
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
