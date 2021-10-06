package com.geode.test;

import com.geode.annotations.mqtt.MqttTopic;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class TopicMan {
        @MqttTopic("test/arthur")
        public void test(MqttMessage message)
        {
            System.out.println("Receive in object: " + new String(message.getPayload()));
        }
}
