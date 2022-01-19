package com.geode.net.net.mqtt;

public interface MqttLostConnexion
{
    void handle(Throwable throwable);
}
