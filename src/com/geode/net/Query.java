package com.geode.net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Query extends SimpleQuery
{
    private Category category;

    public enum Category
    {
        NORMAL,
        DISCOVERY,
        TOPIC_SUBSCRIBE,
        TOPIC_UNSUBSCRIBE,
        TOPIC_NOTIFY,
        TOPIC_NOTIFY_OTHERS,
        QUEUE_SUBSCRIBE,
        QUEUE_UNSUBSCRIBE,
        QUEUE_PRODUCE,
        QUEUE_CONSUME,
        NOTIFY
    }

    public static final Object SUCCESS = new Object();
    public static final Object FAILED = new Object();

    public static Query simple(String type)
    {
        return new Query(type);
    }

    public static Query queueSubscribe(String type)
    {
        return new Query(type).setCategory(Category.QUEUE_SUBSCRIBE);
    }

    public static Query queueUnsubscribe(String type)
    {
        return new Query(type).setCategory(Category.QUEUE_UNSUBSCRIBE);
    }

    public static Query queueProduce(String type)
    {
        return new Query(type).setCategory(Category.QUEUE_PRODUCE);
    }

    public static Query queueConsume(String type)
    {
        return new Query(type).setCategory(Category.QUEUE_CONSUME);
    }

    public static Query notify(String type, Integer ... clientIds)
    {
        return new Query(type).setCategory(Category.NOTIFY).pack(new ArrayList<>(Arrays.asList(clientIds)));
    }

    public static Query notify(String type, ArrayList<Integer> clientIds)
    {
        return new Query(type).setCategory(Category.NOTIFY).pack(clientIds);
    }

    public static Query topicNotify(String type)
    {
        return new Query(type).setCategory(Category.TOPIC_NOTIFY);
    }

    public static Query topicNotifyOthers(String type)
    {
        return new Query(type).setCategory(Category.TOPIC_NOTIFY_OTHERS);
    }

    public static Query success(String type)
    {
        return new Query(type + "_success");
    }

    public static Query failed(String type)
    {
        return new Query(type + "_failed");
    }

    public Query()
    {
        this("q");
    }

    public Query(String type)
    {
        this(type, new ArrayList<>());
    }

    public Query(String type, Serializable ... args)
    {
        super(type, args);
        category = Category.NORMAL;
    }

    public Query(String type, ArrayList<Serializable> args)
    {
        super(type, args);
        category = Category.NORMAL;
    }

    public Query pack(Serializable ... args)
    {
        this.args.addAll(Arrays.asList(args));
        return this;
    }

    public Category getCategory()
    {
        return category;
    }

    public Query setCategory(Category category)
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
