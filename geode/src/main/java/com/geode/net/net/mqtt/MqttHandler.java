package com.geode.net.net.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface MqttHandler
{
    void handle(MqttMessage mqttMessage);
}
