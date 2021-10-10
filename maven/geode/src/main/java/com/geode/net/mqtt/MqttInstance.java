package com.geode.net.mqtt;

import com.geode.annotations.mqtt.MqttTopic;
import com.geode.logging.Logger;
import com.geode.net.Initializable;
import com.geode.net.tls.TLSUtils;
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
    private static final Logger logger = new Logger(MqttInstance.class);

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
        logger.info("initialisation");
        try
        {
            String url = (mqttInfos.isTLSEnable() ? "ssl" : "tcp") + "://" + mqttInfos.getBrokerIp() + ":" + mqttInfos.getBrokerPort();
            mqttClient = new MqttClient(url,
                    mqttInfos.getClientId(),
                    new MemoryPersistence()
            );
            logger.info("mqtt client created at : " + url + " as " + mqttInfos.getClientId());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setAutomaticReconnect(true);
            connectOptions.setCleanSession(true);
            if (mqttInfos.isTLSEnable())
            {
                configTLS(connectOptions);
            }
            mqttClient.setCallback(this);
            logger.info("connect...");
            mqttClient.connect(connectOptions);
            logger.info("connected");
            if (mqttInfos.getTopicsClass() != null)
            {
                logger.info("init message handlers");
                topicsHandler = getMqttInfos().getTopicsClass().getDeclaredConstructor().newInstance();
                subscribe();
                loop();
            }
            connected = true;
        } catch (MqttException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    private void configTLS(MqttConnectOptions connectOptions)
    {
        try
        {
            SocketFactory socketFactory = TLSUtils.getSocketFactory(mqttInfos);
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
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        try
        {
            logger.info("publish " + content + " on " + topic + " topic");
            mqttClient.publish(topic, message);
        } catch (MqttException e)
        {
            e.printStackTrace();
            logger.error("publication failed");
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
            int[] qos = extractTopics();
            logger.info("subscribe to " + Arrays.toString(topicsMap.keySet().toArray(new String[0])));
            mqttClient.subscribe(
                    topicsMap.keySet().toArray(new String[0]),
                    qos
            );
        } catch (MqttException e)
        {
            e.printStackTrace();
            logger.error("unable to subscribe");
        }
    }

    public void subscribe(String topic, int qos)
    {
        try
        {
            logger.info("subscribe to " + topic);
            mqttClient.subscribe(topic, qos);
        } catch (MqttException e)
        {
            e.printStackTrace();
            logger.error("unable to subscribe");
        }
    }

    public void unsubscribe(String topic)
    {
        try
        {
            logger.error("unsubscribe of " + topic);
            mqttClient.unsubscribe(topic);
        } catch (MqttException e)
        {
            e.printStackTrace();
            logger.error("unable to unsubscribe");
        }
    }

    public void addDynHandler(String topic, MqttHandler mqttHandler)
    {
        logger.info("add dynamic handler for " + topic + " topic");
        topicsDynMap.put(topic, mqttHandler);
    }

    public void subDynHandler(String topic)
    {
        logger.error("sub dynamic handler for " + topic + " topic\"");
        topicsDynMap.remove(topic);
    }

    public void loop()
    {
        subscriberThread = new Thread(() -> {
            latch = new CountDownLatch(1);
            try
            {
                logger.info("listening start");
                latch.await();
                logger.info("listening end");
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
        if (mqttClient.isConnected())
        {
            mqttClient.disconnect();
        }
        if (latch != null)
            latch.countDown();
        connected = false;
        logger.info("mqtt client disconnected");
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
        logger.info("connection lost : " + throwable.getMessage());
        if (this.mqttLostConnexionHandler != null)
            mqttLostConnexionHandler.handle(throwable);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception
    {
        logger.info("capture mqtt message : " + mqttMessage);
        if (topicsMap.containsKey(s))
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
            logger.info("message delivered: " + iMqttDeliveryToken.getMessage());
            if (this.mqttMessageSentHandler != null)
                mqttMessageSentHandler.handle(iMqttDeliveryToken);
        } catch (MqttException e)
        {
            e.printStackTrace();
            logger.error("mqtt error : " + e.getMessage());
        }
    }
}
