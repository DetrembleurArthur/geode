package com.geode.net.mqtt;

import com.geode.net.Initializable;
import com.geode.net.annotations.mqtt.MqttTopic;
import com.geode.net.tls.TLSUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.net.SocketFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class MqttInstance implements Initializable, AutoCloseable, MqttCallback
{
    private static final Logger logger = LogManager.getLogger(MqttInstance.class);

    private final MqttInfos mqttInfos;
    private MqttClient mqttClient;
    private Object topicsHandler;
    private HashMap<String, Method> topicsMap;
    private HashMap<String, MqttHandler> topicsDynMap;
    private CountDownLatch latch;
    private Thread subscriberThread;
    private MqttLostConnexion mqttLostConnexionHandler;
    private MqttMessageSent mqttMessageSentHandler;
    private boolean connected = false;

    public MqttInstance(MqttInfos mqttInfos)
    {
        this.mqttInfos = mqttInfos;
        this.topicsDynMap = new HashMap<>();
    }

    public MqttInfos getMqttInfos()
    {
        return mqttInfos;
    }

    @Override
    public void init()
    {
        logger.info("initialisation", getMqttInfos().getName());
        try
        {
            String url = (mqttInfos.getTlsInfos().isTLSEnable() ? "ssl" : "tcp") + "://" + mqttInfos.getBrokerIp() + ":" + mqttInfos.getBrokerPort();
            mqttClient = new MqttClient(url,
                    mqttInfos.getClientId(),
                    new MemoryPersistence()
            );
            logger.info("mqtt client created at : " + url + " as " + mqttInfos.getClientId(), getMqttInfos().getName());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setAutomaticReconnect(true);
            connectOptions.setCleanSession(true);
            if (mqttInfos.getTlsInfos().isTLSEnable())
            {
                configTLS(connectOptions);
            }
            mqttClient.setCallback(this);
            logger.info("connect...", getMqttInfos().getName());
            mqttClient.connect(connectOptions);
            logger.info("connected", getMqttInfos().getName());
            if (mqttInfos.isSubscriber())
            {
                logger.info("init subscription", getMqttInfos().getName());
                if(mqttInfos.getTopicsClass() != null)
                {
                    topicsHandler = getMqttInfos().getTopicsClass().getDeclaredConstructor().newInstance();
                    subscribe();
                }
                loop();
            }
            connected = true;
        } catch (MqttException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
            logger.fatal("initialisation error : " + e, getMqttInfos().getName());
        }
    }

    private void configTLS(MqttConnectOptions connectOptions)
    {
        try
        {
            SocketFactory socketFactory = TLSUtils.getSocketFactory(mqttInfos.getTlsInfos());
            connectOptions.setSocketFactory(socketFactory);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean isConnected()
    {
        return connected;
    }

    public boolean publish(String topic, String content, int qos)
    {
        try
        {
            if(!mqttInfos.isPublisher()) throw new Exception("mqtt client is not publisher");
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            logger.info("publish " + content + " on " + topic + " topic", getMqttInfos().getName());
            mqttClient.publish(topic, message);
        } catch (Exception e)
        {
            e.printStackTrace();
            logger.error("publication failed : " + e, getMqttInfos().getName());
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
        for (Object m : methods)
        {
            MqttTopic mqttTopic = ((Method) m).getAnnotation(MqttTopic.class);
            topicsMap.put(mqttTopic.value(), (Method) m);
            qos[i++] = mqttTopic.qos();
        }
        return qos;
    }

    public void subscribe()
    {
        try
        {
            if(!mqttInfos.isSubscriber()) throw new Exception("mqtt client is not subscriber");
            int[] qos = extractTopics();
            logger.info("subscribe to " + Arrays.toString(topicsMap.keySet().toArray(new String[0])), getMqttInfos().getName());
            mqttClient.subscribe(
                    topicsMap.keySet().toArray(new String[0]),
                    qos
            );
        } catch (Exception e)
        {
            e.printStackTrace();
            logger.error("unable to subscribe : " + e, getMqttInfos().getName());
        }
    }

    public void subscribe(String topic, int qos)
    {
        try
        {
            if(!mqttInfos.isSubscriber()) throw new Exception("mqtt client is not subscriber");
            logger.info("subscribe to " + topic, getMqttInfos().getName());
            mqttClient.subscribe(topic, qos);
        } catch (Exception e)
        {
            e.printStackTrace();
            logger.error("unable to subscribe : " + e, getMqttInfos().getName());
        }
    }

    public void unsubscribe(String topic)
    {
        try
        {
            if(!mqttInfos.isSubscriber()) throw new Exception("mqtt client is not subscriber");
            logger.error("unsubscribe of " + topic, getMqttInfos().getName());
            mqttClient.unsubscribe(topic);
        } catch (Exception e)
        {
            e.printStackTrace();
            logger.error("unable to unsubscribe : " + e, getMqttInfos().getName());
        }
    }

    public void addDynHandler(String topic, MqttHandler mqttHandler)
    {
        logger.info("add dynamic handler for " + topic + " topic", getMqttInfos().getName());
        topicsDynMap.put(topic, mqttHandler);
    }

    public void subDynHandler(String topic)
    {
        logger.error("sub dynamic handler for " + topic + " topic\"", getMqttInfos().getName());
        topicsDynMap.remove(topic);
    }

    public void loop()
    {
        subscriberThread = new Thread(() -> {
            latch = new CountDownLatch(1);
            try
            {
                if(!mqttInfos.isSubscriber()) throw new Exception("mqtt client is not subscriber");
                logger.info("listening start", getMqttInfos().getName());
                latch.await();
                logger.info("listening end", getMqttInfos().getName());
            } catch (Exception e)
            {
                e.printStackTrace();
                logger.warn("mqtt thread interrupted : " + e, getMqttInfos().getName());
            }
        });
        subscriberThread.setDaemon(true);
        subscriberThread.start();
    }

    @Override
    public void close() throws Exception
    {
        if (mqttClient.isConnected())
        {
            mqttClient.disconnect();
            connected = false;
            logger.info("mqtt client disconnected", getMqttInfos().getName());
        }
        if (latch != null)
            latch.countDown();
        subscriberThread.join();
        logger.info("mqtt client down", getMqttInfos().getName());
    }

    public void setMqttLostConnexionHandler(MqttLostConnexion mqttLostConnexionHandler)
    {
        this.mqttLostConnexionHandler = mqttLostConnexionHandler;
    }

    public void setMqttMessageSentHandler(MqttMessageSent mqttMessageSentHandler)
    {
        this.mqttMessageSentHandler = mqttMessageSentHandler;
    }

    @Override
    public void connectionLost(Throwable throwable)
    {
        logger.info("connection lost : " + throwable.getMessage(), getMqttInfos().getName());
        if (this.mqttLostConnexionHandler != null)
            mqttLostConnexionHandler.handle(throwable);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception
    {
        logger.info("capture mqtt message : " + mqttMessage, getMqttInfos().getName());
        if (topicsHandler != null && topicsMap.containsKey(s))
        {
            topicsMap.get(s).invoke(topicsHandler, mqttMessage);
        }
        if (topicsDynMap.containsKey(s))
        {
            topicsDynMap.get(s).handle(mqttMessage);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
    {
        try
        {
            logger.info("message delivered: " + iMqttDeliveryToken.getMessage(), getMqttInfos().getName());
            if (this.mqttMessageSentHandler != null)
                mqttMessageSentHandler.handle(iMqttDeliveryToken);
        } catch (MqttException e)
        {
            e.printStackTrace();
            logger.error("mqtt error : " + e, getMqttInfos().getName());
        }
    }
}
