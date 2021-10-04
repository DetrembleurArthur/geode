package com.geode.test;

import com.geode.annotations.mqtt.MqttTopic;
import com.geode.net.mqtt.MqttSubscriber;
import com.geode.net.mqtt.MqttSubscriberInfos;


public class Sub {

    

    public static void main(String[] args) throws Exception {
        
        MqttSubscriber subscriber = new MqttSubscriber(
            new MqttSubscriberInfos("192.168.1.48", 1883, (byte)0, "Arthur")
        );

        subscriber.getSubscriberInfos().setTopicCluster(TopicMan.class);

        subscriber.init();
        subscriber.subscribe();
        System.in.read();
        subscriber.close();
    }
}
