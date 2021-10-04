package com.geode.net.mqtt;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.geode.annotations.mqtt.MqttTopic;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttSubscriber extends MqttRunner implements IMqttMessageListener
{
    private HashMap<String, Method> topicHandlers;
    private Object topicRunner;
    CountDownLatch latch;

    public MqttSubscriber(MqttSubscriberInfos infos)
    {
        super(infos);
    }

    public MqttSubscriberInfos getSubscriberInfos()
    {
        return (MqttSubscriberInfos)getInfos();
    }

    private String[] extractTopics()
    {
        topicHandlers = new HashMap<>();
        for(Method m : topicRunner.getClass().getDeclaredMethods())
        {
            System.out.println(m.getAnnotation(MqttTopic.class));
            if(m.isAnnotationPresent(MqttTopic.class))
            {System.out.println("OK");
                if(m.getParameterCount() == 1 && m.getParameterTypes()[0].equals(MqttMessage.class))
                {
                    topicHandlers.put(m.getAnnotation(MqttTopic.class).value(), m);
                }
            }
        }
        String[] topics = new String[topicHandlers.keySet().size()];
        int i = 0;
        for(String t : topicHandlers.keySet())
        {
            System.out.println(t);
            topics[i++] = t;
        }
        return topics;
    }

    public void subscribe()
    {
        try {
            topicRunner = getSubscriberInfos().getTopicCluster().getConstructor().newInstance();
            latch = new CountDownLatch(10);

            this.getClient().subscribe(extractTopics(),new int[]{0},
            new IMqttMessageListener[]{this});
            latch.await(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception
    {
        if(topicHandlers.containsKey(topic))
        {
            topicHandlers.get(topic).invoke(topicRunner, message);
        }
        latch.countDown();
        System.err.println(latch.getCount());
    }
}
