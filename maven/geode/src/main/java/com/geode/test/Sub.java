package com.geode.test;

import com.geode.net.mqtt.MqttInfos;
import com.geode.net.mqtt.MqttInstance;
import com.geode.net.mqtt_proto.MqttSubscriber;
import com.geode.net.mqtt_proto.MqttSubscriberInfos;

import java.util.Scanner;


public class Sub {

    

    public static void main(String[] args) throws Exception {

        MqttInstance instance = new MqttInstance(new MqttInfos("test.mosquitto.org", 1883, 0, "pub1", TopicMan.class));
        instance.init();

        instance.subscribe();
        instance.loop();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        instance.close();
    }
}
