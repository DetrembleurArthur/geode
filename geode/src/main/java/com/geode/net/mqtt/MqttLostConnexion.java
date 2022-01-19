package com.geode.net.mqtt;

public interface MqttLostConnexion
{
    void handle(Throwable throwable);
}
