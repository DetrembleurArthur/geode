package com.geode.net.net.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

public interface MqttMessageSent
{
    void handle(IMqttDeliveryToken iMqttDeliveryToken);
}
