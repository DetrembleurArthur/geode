package com.geode.net.mqtt;

import com.geode.net.Initializable;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MqttRunner implements Initializable, AutoCloseable
{
    private MqttInfos infos;
    private MqttClient client;

    public MqttRunner(MqttInfos infos)
    {
        this.infos = infos;
    }

    public MqttInfos getInfos()
    {
        return infos;
    }

    public MqttClient getClient()
    {
        return client;
    }

    @Override
    public void init()
    {
        try
        {
            client = new MqttClient(
                "tcp://" + infos.getBrokerIp() + ":" +
                Integer.toString(infos.getBrokerPort()),
                infos.getClientId(),
                new MemoryPersistence());
            MqttConnectOptions connOpt = new MqttConnectOptions();
            connOpt.setCleanSession(true);
            client.connect(connOpt);
        } catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception
    {
        client.disconnect();
    }
}
