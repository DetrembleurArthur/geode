package com.geode.net.mqtt;

import com.geode.net.tls.TLSInfos;

public class MqttInfos
{
    private TLSInfos tlsInfos = new TLSInfos();
    private String name;
    private String brokerIp;
    private int brokerPort;
    private int defaultQos;
    private String clientId;
    private Class<?> topicsClass;
    private String profile;


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

    

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        profile = profile.toLowerCase();
        switch(profile)
        {
            case "pub":
            case "sub":
            case "pub-sub":break;
            default: throw new RuntimeException(profile + " is not a valid profile...");
        }
        this.profile = profile;
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

    public boolean isSubscriber()
    {
        return profile.contains("sub");
    }

    public boolean isPublisher()
    {
        return profile.contains("pub");
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    

    public TLSInfos getTlsInfos() {
        return tlsInfos;
    }

    public void setTlsInfos(TLSInfos tlsInfos) {
        this.tlsInfos = tlsInfos;
    }

    @Override
    public String toString()
    {
        return "MqttInfos{" +
                "name='" + name + '\'' +
                ", brokerIp='" + brokerIp + '\'' +
                ", brokerPort=" + brokerPort +
                ", defaultQos=" + defaultQos +
                ", clientId='" + clientId + '\'' +
                ", topicsClass=" + topicsClass +
                ", profile='" + profile + '\'' +
                '}';
    }
}
