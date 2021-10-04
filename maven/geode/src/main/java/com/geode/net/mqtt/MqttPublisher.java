package com.geode.net.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class MqttPublisher extends MqttRunner
{

    public MqttPublisher(MqttInfos infos)
    {
        super(infos);
    }
    
    public void publish(String topic, String content) throws MqttPersistenceException, MqttException
    {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(this.getInfos().getQos());
        getClient().publish(topic, message);
    }
}
