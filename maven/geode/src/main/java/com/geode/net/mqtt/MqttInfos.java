package com.geode.net.mqtt;

public class MqttInfos
{
    private String brokerIp;
    private int brokerPort;
    private byte qos;
    private String clientId;

    

    public MqttInfos() {
    }

    public MqttInfos(String brokerIp, int brokerPort, byte qos, String clientId) {
        this.brokerIp = brokerIp;
        this.brokerPort = brokerPort;
        this.qos = qos;
        this.clientId = clientId;
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

    public byte getQos() {
        return qos;
    }
    
    public void setQos(byte qos) throws Exception {
        if(qos < 0 || qos > 2) throw new Exception("QoS must be between 0 and 2, not " + Integer.toString(qos));
        this.qos = qos;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
