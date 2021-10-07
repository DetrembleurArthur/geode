package com.geode.net.mqtt;

import com.geode.net.tls.TLSInfos;

public class MqttInfos extends TLSInfos
{
    private String brokerIp;
    private int brokerPort;
    private int defaultQos;
    private String clientId;
    private Class<?> topicsClass;


    public MqttInfos() {
    }

    public MqttInfos(String brokerIp, int brokerPort, int defaultQos, String clientId, Class<?> topicsClass)
    {
        this.brokerIp = brokerIp;
        this.brokerPort = brokerPort;
        this.defaultQos = defaultQos;
        this.clientId = clientId;
        this.topicsClass = topicsClass;
    }

    public Class<?> getTopicsClass()
    {
        return topicsClass;
    }

    public void setTopicsClass(Class<?> topicsClass)
    {
        this.topicsClass = topicsClass;
    }

    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    public int getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(int brokerPort) {
        this.brokerPort = brokerPort;
    }

    public int getDefaultQos() {
        return defaultQos;
    }

    public void setDefaultQos(int defaultQos) throws Exception {
        if(defaultQos < 0 || defaultQos > 2) throw new Exception("QoS must be between 0 and 2, not " + Integer.toString(defaultQos));
        this.defaultQos = defaultQos;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
