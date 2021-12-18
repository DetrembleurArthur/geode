package com.geode.net;


import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The type Queue.
 */
public class Queue
{
    private final ConcurrentLinkedQueue<GeodeQuery> queue;

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
     * @param geodeQuery the query
     */
    public synchronized void produce(GeodeQuery geodeQuery)
    {
        queue.add(geodeQuery);
    }

    /**
     * Consume query.
     *
     * @return the query
     */
    public synchronized GeodeQuery consume()
    {
        return queue.poll();
    }
}
