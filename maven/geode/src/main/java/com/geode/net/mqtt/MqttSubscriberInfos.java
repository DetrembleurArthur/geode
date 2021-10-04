package com.geode.net.mqtt;

public class MqttSubscriberInfos extends MqttInfos
{
    private Class<?> topicCluster;

    
    public MqttSubscriberInfos() {
    }

    public MqttSubscriberInfos(String brokerIp, int brokerPort, byte qos, String clientId) {
        super(brokerIp, brokerPort, qos, clientId);
    }

    public MqttSubscriberInfos(Class<?> topicCluster)
    {
        this.topicCluster = topicCluster;
    }

    public Class<?> getTopicCluster()
    {
        return topicCluster;
    }

    public void setTopicCluster(Class<?> topicCluster)
    {
        this.topicCluster = topicCluster;
    }
}
