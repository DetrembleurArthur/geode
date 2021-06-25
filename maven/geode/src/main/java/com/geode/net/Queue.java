package com.geode.net;


import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The type Queue.
 */
public class Queue
{
    private final ConcurrentLinkedQueue<Query> queue;

    /**
     * Instantiates a new Queue.
     */
    public Queue()
    {
        queue = new ConcurrentLinkedQueue<>();
    }

    /**
     * Produce.
     *
     * @param query the query
     */
    public synchronized void produce(Query query)
    {
        queue.add(query);
    }

    /**
     * Consume query.
     *
     * @return the query
     */
    public synchronized Query consume()
    {
        return queue.poll();
    }
}
