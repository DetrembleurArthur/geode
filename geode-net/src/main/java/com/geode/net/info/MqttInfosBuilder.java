package com.geode.net.info;

import com.geode.net.annotations.Attribute;
import com.geode.net.mqtt.MqttInfos;
import com.geode.net.tls.TLSInfos;

public class MqttInfosBuilder extends Builder<MqttInfos>
{
    static
    {
        BuildersMap.register(MqttInfos.class, MqttInfosBuilder.class);
    }

    public MqttInfosBuilder()
    {
        reset();
    }

    public static MqttInfosBuilder create()
    {
        return new MqttInfosBuilder();
    }

    @Override
    public MqttInfosBuilder reset()
    {
        object = new MqttInfos();
        return this;
    }

    @Attribute("broker-ip")
    public MqttInfosBuilder brokerIp(String brokerIp)
    {
        object.setBrokerIp(brokerIp);
        return this;
    }

    @Attribute("broker-port")
    public MqttInfosBuilder brokerPort(int brokerPort)
    {
        object.setBrokerPort(brokerPort);
        return this;
    }

    @Attribute("client-id")
    public MqttInfosBuilder clientId(String clientId)
    {
        object.setClientId(clientId);
        return this;
    }

    @Attribute("default-qos")
    public MqttInfosBuilder defaultQos(int defaultQos)
    {
        try {
            object.setDefaultQos(defaultQos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Attribute("name")
    public MqttInfosBuilder name(String name)
    {
        object.setName(name);
        return this;
    }

    @Attribute("profile")
    public MqttInfosBuilder profile(String profile)
    {
        object.setProfile(profile);
        return this;
    }

    @Attribute("tls")
    public MqttInfosBuilder tlsInfos(TLSInfos tlsInfos)
    {
        object.setTlsInfos(tlsInfos);
        return this;
    }

    @Attribute("topic-class")
    public MqttInfosBuilder topicClass(String topiClass)
    {
        try {
            object.setTopicsClass(Class.forName(topiClass));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }
}
