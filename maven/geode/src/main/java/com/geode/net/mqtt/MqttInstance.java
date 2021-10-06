package com.geode.net.mqtt;

import com.geode.annotations.mqtt.MqttTopic;
import com.geode.net.Initializable;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

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
                    "ssl://" + mqttInfos.getBrokerIp() + ":" + mqttInfos.getBrokerPort(),
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
            SocketFactory socketFactory = getSocketFactory(mqttInfos.getCafile());
            connectOptions.setSocketFactory(socketFactory);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    static X509Certificate readCert(final String fname) throws CertificateException, FileNotFoundException
    {
        return (X509Certificate) CertificateFactory
                .getInstance("X.509")
                .generateCertificate(new FileInputStream(fname));
    }

    static KeyStore loadKeystore(final String keystoreFile, final String password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException
    {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(keystoreFile), password.toCharArray());
        return ks;
    }

    
    public PrivateKey getPrivateKey(String filename) throws Exception {

        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf =
                KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    SSLSocketFactory getSocketFactory (final String caCrtFile) throws Exception
    {
        KeyStore caKs = KeyStore.getInstance("JKS");
        caKs.load(null, null);

        X509Certificate caCert = readCert(caCrtFile);
        
        caKs.setCertificateEntry("CA", caCert);
        System.err.println(caCert);

        X509Certificate cliCert = readCert(mqttInfos.getCertfile());
        caKs.setCertificateEntry("CLIcert", cliCert);

        PrivateKey key = getPrivateKey(mqttInfos.getKeyfile());
        caKs.setKeyEntry("CLIkey", key, "".toCharArray(), new Certificate[]{cliCert});
        
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");


        tmf.init(caKs);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(caKs, "".toCharArray());

        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context.getSocketFactory();
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
