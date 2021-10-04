package com.geode.test;

import java.util.Scanner;

import com.geode.net.mqtt.MqttInfos;
import com.geode.net.mqtt.MqttPublisher;

public class Pub {
    public static void main(String[] args) throws Exception {
        MqttPublisher publisher = new MqttPublisher(
            new MqttInfos("192.168.1.48", 1883, (byte)0, "ArthurS")
        );

        publisher.init();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("topic: ");
        String topic = scanner.nextLine();

        System.out.println("message: ");
        String message = scanner.nextLine();

        publisher.publish(topic, message);

        publisher.close();
        scanner.close();
    }
}
