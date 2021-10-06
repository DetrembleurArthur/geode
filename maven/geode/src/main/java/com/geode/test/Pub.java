package com.geode.test;

import com.geode.net.Geode;
import com.geode.net.mqtt.MqttInstance;

public class Pub {
    public static void main(String[] args) throws Exception {
        Geode geode = new Geode();
        geode.init("src/main/resources/conf.yml");

        MqttInstance mqttInstance = geode.launchMqtt("test");

        for(int i = 0; i < 10; i++){
            mqttInstance.publish("test/arthur", "Hello world from geode!");
            Thread.sleep(500);
        }
        mqttInstance.close();
    }
}