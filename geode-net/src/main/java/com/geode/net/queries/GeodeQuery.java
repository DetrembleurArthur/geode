package com.geode.net.queries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The type Query.
 */
public class GeodeQuery extends SimpleQuery
{
    /**
     * The constant SUCCESS.
     */
    public static final Object SUCCESS = new Object();
    /**
     * The constant FAILED.
     */
    public static final Object FAILED = new Object();
    public static final Object NO_QUERY = null;
    private Category category;

    /**
     * Instantiates a new Query.
     */
    public GeodeQuery()
    {
        this("q");
    }

    /**
     * Instantiates a new Query.
     *
     * @param type the type
     */
    public GeodeQuery(String type)
    {
        this(type, new ArrayList<>());
    }

    /**
     * Instantiates a new Query.
     *
     * @param type the type
     * @param args the args
     */
    public GeodeQuery(String type, Serializable... args)
    {
        super(type, args);
        category = Category.NORMAL;
    }

    /**
     * Instantiates a new Query.
     *
     * @param type the type
     * @param args the args
     */
    public GeodeQuery(String type, ArrayList<Serializable> args)
    {
        super(type, args);
        category = Category.NORMAL;
    }

    /**
     * Simple query.
     *
     * @param type the type
     * @return the query
     */
    public static GeodeQuery simple(String type)
    {
        return new GeodeQuery(type);
    }

    /**
     * Queue subscribe query.
     *
     * @param type the type
     * @return the query
     */
    public static GeodeQuery queueSubscribe(String type)
    {
        return new GeodeQuery(type).setCategory(Category.QUEUE_SUBSCRIBE);
    }

    /**
     * Queue unsubscribe query.
     *
     * @param type the type
     * @return the query
     */
    public static GeodeQuery queueUnsubscribe(String type)
    {
        return new GeodeQuery(type).setCategory(Category.QUEUE_UNSUBSCRIBE);
    }

    /**
     * Queue produce query.
     *
     * @param type the type
     * @return the query
     */
    public static GeodeQuery queueProduce(String type)
    {
        return new GeodeQuery(type).setCategory(Category.QUEUE_PRODUCE);
    }

    /**
     * Queue consume query.
     *
     * @param type the type
     * @return the query
     */
    public static GeodeQuery queueConsume(String type)
    {
        return new GeodeQuery(type).setCategory(Category.QUEUE_CONSUME);
    }

    /**
     * Notify query.
     *
     * @param type      the type
     * @param clientIds the client ids
     * @return the query
     */
    public static GeodeQuery notify(String type, Integer... clientIds)
    {
        return new GeodeQuery(type).setCategory(Category.NOTIFY).pack(new ArrayList<>(Arrays.asList(clientIds)));
    }

    /**
     * Notify query.
     *
     * @param type      the type
     * @param clientIds the client ids
     * @return the query
     */
    public static GeodeQuery notify(String type, ArrayList<Integer> clientIds)
    {
        return new GeodeQuery(type).setCategory(Category.NOTIFY).pack(clientIds);
    }

    /**
     * Topic notify query.
     *
     * @param type the type
     * @return the query
     */
    public static GeodeQuery topicNotify(String type)
    {
        return new GeodeQuery(type).setCategory(Category.TOPIC_NOTIFY);
    }

    /**
     * Topic notify others query.
     *
     * @param type the type
     * @return the query
     */
    public static GeodeQuery topicNotifyOthers(String type)
    {
        return new GeodeQuery(type).setCategory(Category.TOPIC_NOTIFY_OTHERS);
    }

    /**
     * Success query.
     *
     * @param type the type
     * @return the query
     */
    public static GeodeQuery success(String type)
    {
        return new GeodeQuery(type + "_success");
    }

    /**
     * Failed query.
     *
     * @param type the type
     * @return the query
     */
    public static GeodeQuery failed(String type)
    {
        return new GeodeQuery(type + "_failed");
    }

    public GeodeQuery pack(Serializable... args)
    {
        this.args.addAll(Arrays.asList(args));
        return this;
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public Category getCategory()
    {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category the category
     * @return the category
     */
    public GeodeQuery setCategory(Category category)
    {
        this.category = category;
        return this;
    }

    public LowQuery low()
    {
        return new LowQuery(this);
    }


    @Override
    public String toString()
    {
        return "Q [" + type + "::" + category + "::" + args + "]";
    }

    /**
     * The enum Category.
     */
    public enum Category
    {
        /**
         * Normal category.
         */
        NORMAL,
        /**
         * Discovery category.
         */
        DISCOVERY,
        /**
         * Topic subscribe category.
         */
        TOPIC_SUBSCRIBE,
        /**
         * Topic unsubscribe category.
         */
        TOPIC_UNSUBSCRIBE,
        /**
         * Topic notify category.
         */
        TOPIC_NOTIFY,
        /**
         * Topic notify others category.
         */
        TOPIC_NOTIFY_OTHERS,
        /**
         * Queue subscribe category.
         */
        QUEUE_SUBSCRIBE,
        /**
         * Queue unsubscribe category.
         */
        QUEUE_UNSUBSCRIBE,
        /**
         * Queue produce category.
         */
        QUEUE_PRODUCE,
        /**
         * Queue consume category.
         */
        QUEUE_CONSUME,
        /**
         * Notify category.
         */
        NOTIFY
    }
}
