package com.geode.test;

import com.geode.logging.Logger;
import com.geode.net.Geode;
import com.geode.net.mqtt.MqttInstance;

public class Pub {
    public static void main(String[] args) throws Exception {

       // System.setProperty("javax.net.debug", "all");

        Logger.setFile("C:\\Users\\mb624\\Desktop\\log.txt");
        Geode geode = new Geode();
        geode.init("src/main/resources/conf.yml");

        MqttInstance mqttInstance = geode.launchMqtt("mosquitto");

        for(int i = 0; i < 3; i++){
            mqttInstance.publish("test/arthur", "Hello world from geode!");
            Thread.sleep(500);
        }
        mqttInstance.close();
    }
}
