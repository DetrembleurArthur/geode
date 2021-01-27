package com.geode.net;


import java.util.concurrent.ConcurrentLinkedQueue;

public class Queue
{
    private final ConcurrentLinkedQueue<Query> queue;

    public Queue()
    {
        queue = new ConcurrentLinkedQueue<>();
    }

    public synchronized void produce(Query query)
    {
        queue.add(query);
    }

    public synchronized Query consume()
    {
        return queue.poll();
    }
}
