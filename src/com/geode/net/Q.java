package com.geode.net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Q implements Serializable
{
    private String type;
    private Category category;
    private ArrayList<Serializable> args;

    public enum Category
    {
        NORMAL,
        DISCOVERY,
        TOPIC_SUBSCRIBE,
        TOPIC_UNSUBSCRIBE,
        TOPIC_NOTIFY,
        TOPIC_NOTIFY_OTHERS
    }

    public static final Object SUCCESS = new Object();
    public static final Object FAILED = new Object();

    public static Q simple(String type)
    {
        return new Q(type);
    }

    public static Q notify(String type)
    {
        return new Q(type).setCategory(Category.TOPIC_NOTIFY);
    }

    public static Q notifyOthers(String type)
    {
        return new Q(type).setCategory(Category.TOPIC_NOTIFY_OTHERS);
    }

    public static Q success(String type)
    {
        return new Q(type + "_success");
    }

    public static Q failed(String type)
    {
        return new Q(type + "_failed");
    }

    public Q()
    {
        this("q");
    }

    public Q(String type)
    {
        this(type, new ArrayList<>());
    }

    public Q(String type, Serializable ... args)
    {
        this.args = (ArrayList<Serializable>) Arrays.asList(args);
        setType(type);
        category = Category.NORMAL;
    }

    public Q(String type, ArrayList<Serializable> args)
    {
        this.args = args;
        setType(type);
        category = Category.NORMAL;
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

    public Q pack(Serializable ... args)
    {
        this.args.addAll(Arrays.asList(args));
        return this;
    }

    public Category getCategory()
    {
        return category;
    }

    public Q setCategory(Category category)
    {
        this.category = category;
        return this;
    }

    @Override
    public String toString()
    {
        return "Q [" + type + "::" + category + "::" + args + "]";
    }
}
