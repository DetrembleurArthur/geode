package com.geode.test;

import com.geode.logging.Level;
import com.geode.logging.Logger;
import com.geode.net.Geode;
import com.geode.net.mqtt.MqttInstance;

public class Pub {
    public static void main(String[] args) throws Exception {

       // System.setProperty("javax.net.debug", "all");

        Logger.setFile("C:\\Users\\mb624\\Desktop\\log.txt", true);
        Logger.setLevel(Level.DEBUG);

        Geode geode = new Geode();
        geode.init("src/main/resources/conf.yml");

        MqttInstance mqttInstance = geode.launchMqtt("mosquitto");
        mqttInstance.subscribe("test/arthur", 0);
        mqttInstance.addDynHandler("test/arthur", m -> {
            System.out.println(m);
        });

        for(int i = 0; i < 3; i++){
            mqttInstance.publish("test/arthur", "Hello world from geode!");
            Thread.sleep(500);
        }
        mqttInstance.close();
        
        System.out.println("bye!");
    }
}
