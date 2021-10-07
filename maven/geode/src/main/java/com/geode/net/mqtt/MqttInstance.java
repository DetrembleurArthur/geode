package com.geode.net.mqtt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import javax.net.SocketFactory;

import com.geode.annotations.mqtt.MqttTopic;
import com.geode.net.Initializable;
import com.geode.net.tls.TLSUtils;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttInstance implements Initializable, AutoCloseable, MqttCallback
{
    private final MqttInfos mqttInfos;
    private MqttClient mqttClient;
    private Object topicsHandler;
    private HashMap<String, Method> topicsMap;
    private CountDownLatch latch;
    private Thread subscriberThread;

    public MqttInstance(MqttInfos mqttInfos)
    {
        this.mqttInfos = mqttInfos;
    }

    public MqttInfos getMqttInfos()
    {
        return mqttInfos;
    }

    @Override
    public void init()
    {
        try
        {
            mqttClient = new MqttClient(
                    (mqttInfos.isTLSEnable() ? "ssl" : "tcp") + "://" + mqttInfos.getBrokerIp() + ":" + mqttInfos.getBrokerPort(),
                    mqttInfos.getClientId(),
                    new MemoryPersistence()
            );
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setAutomaticReconnect(true);
            connectOptions.setCleanSession(true);
            if(mqttInfos.getCafile() != null)
            {
                configTLS(connectOptions);
            }
            mqttClient.setCallback(this);
            mqttClient.connect(connectOptions);
            if(mqttInfos.getTopicsClass() != null)
            {
                topicsHandler = getMqttInfos().getTopicsClass().getDeclaredConstructor().newInstance();
                subscribe();
                loop();
            }
        } catch (MqttException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    private void configTLS(MqttConnectOptions connectOptions)
    {
        try
        {
            SocketFactory socketFactory = TLSUtils.getSocketFactory(
                mqttInfos.getCafile(), mqttInfos.getCertfile(), mqttInfos.getKeyfile());
            connectOptions.setSocketFactory(socketFactory);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    

    public boolean publish(String topic, String content, int qos)
    {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        try
        {
            mqttClient.publish(topic, message);
        } catch (MqttException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean publish(String topic, String message)
    {
        return publish(topic, message, mqttInfos.getDefaultQos());
    }

    private int[] extractTopics()
    {
        topicsMap = new HashMap<>();
        Object[] methods = Arrays.stream(mqttInfos.getTopicsClass().getDeclaredMethods()).filter(
                method -> method.isAnnotationPresent(MqttTopic.class) && method.getParameterTypes()[0].equals(MqttMessage.class)
        ).toArray();
        int[] qos = new int[methods.length];
        int i = 0;
        for(Object m : methods)
        {
            MqttTopic mqttTopic = ((Method)m).getAnnotation(MqttTopic.class);
            topicsMap.put(mqttTopic.value(), (Method)m);
            qos[i++] = mqttTopic.qos();
        }
        return qos;
    }

    public void subscribe()
    {
        try
        {
            int[] qos = extractTopics();
            mqttClient.subscribe(
                    topicsMap.keySet().toArray(new String[0]),
                    qos
            );
        } catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic, int qos)
    {
        try
        {
            mqttClient.subscribe(topic, qos);
        } catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

    public void unsubscribe(String topic)
    {
        try
        {
            mqttClient.unsubscribe(topic);
        } catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

    public void loop()
    {
        subscriberThread = new Thread(() -> {
            latch = new CountDownLatch(1);
            try
            {
                System.err.println("await start");
                latch.await();
                System.err.println("await end");
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });
        subscriberThread.setDaemon(true);
        subscriberThread.start();
    }

    @Override
    public void close() throws Exception
    {
        if(mqttClient.isConnected())
        {
            mqttClient.disconnect();
        }
        if(latch != null)
            latch.countDown();
    }

    @Override
    public void connectionLost(Throwable throwable)
    {
        System.err.println(throwable.getMessage());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception
    {
        if(topicsMap.containsKey(s))
        {
            topicsMap.get(s).invoke(topicsHandler, mqttMessage);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
    {
        try
        {
            System.err.println("message delivered: " + iMqttDeliveryToken.getMessage());
        } catch (MqttException e)
        {
            e.printStackTrace();
        }
    }
}
