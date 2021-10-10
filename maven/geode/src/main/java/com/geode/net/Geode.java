package com.geode.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.geode.logging.Logger;
import com.geode.net.mqtt.MqttInfos;
import com.geode.net.mqtt.MqttInstance;
import com.geode.net.tls.TLSInfos;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.yaml.snakeyaml.Yaml;

/**
 * The type Geode.
 */
public final class Geode
{
    static
    {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final Logger logger = new Logger(Geode.class);

    private final HashMap<String, ServerInfos> serversInfos;
    private final HashMap<String, ClientInfos> clientsInfos;
    private final HashMap<String, UdpInfos> udpsInfos;
    private final HashMap<String, MqttInfos> mqttInfos;
    private final ArrayList<Server> servers;
    private final ArrayList<Client> tcpClients;
    private final ArrayList<UdpHandler> udpHandlers;
    private final ArrayList<MqttInstance> mqttInstances;
    private boolean broken;

    /**
     * Instantiates a new Geode.
     */

    /**
     * Instantiates a new Geode.
     */
    public Geode()
    {
        broken = false;
        serversInfos = new HashMap<>();
        clientsInfos = new HashMap<>();
        udpsInfos = new HashMap<>();
        servers = new ArrayList<>();
        tcpClients = new ArrayList<>();
        udpHandlers = new ArrayList<>();
        mqttInstances = new ArrayList<>();
        mqttInfos = new HashMap<>();
    }

    private void tlsInit(TLSInfos infos, Map<String, Object> data)
    {
        if(data.containsKey("tls"))
        {
            infos.setEnable(true);
            data = (Map<String, Object>) data.get("tls");
            if(data.containsKey("ksfile"))
            {
                infos.setKeystore((String)data.getOrDefault("ksfile", null));
            }
            else
            {
                infos.setCafile((String)data.getOrDefault("cafile", null));
                infos.setCertfile((String)data.getOrDefault("certfile", null));
                infos.setKeyfile((String)data.getOrDefault("keyfile", null));
            }
        }
    }

    public void init(String yamlFilename) throws Exception
    {
        logger.info("load configuration file : " + yamlFilename);
        InputStream stream = new FileInputStream(new File(yamlFilename));
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(stream);
        logger.info("data loaded : " + data);
        Map<String, Object> servers = (Map<String, Object>) data.get("servers");
        Map<String, Object> clients = (Map<String, Object>) data.get("clients");
        Map<String, Object> udpHandlers = (Map<String, Object>) data.get("udpHandlers");
        Map<String, Object> mqtt = (Map<String, Object>) data.get("mqtt");
        if(servers != null)
        for(String key : servers.keySet())
        {
            String serverId = key;
            Map<String, Object> serverData = (Map<String, Object>) servers.get(serverId);
            ServerInfos serverInfos = new ServerInfos();
            tlsInit(serverInfos, serverData);
            serverInfos.setHost((String) serverData.getOrDefault("host", "127.0.0.1"));
            serverInfos.setPort((Integer) serverData.getOrDefault("port", 50000));
            serverInfos.setBacklog((Integer) serverData.getOrDefault("backlog", 10));
            ArrayList<String> protocolNames = (ArrayList<String>) serverData.get("protocols");
            if(protocolNames != null)
            {
                serverInfos.setProtocolClasses(new ArrayList<>());
                for(String pName : protocolNames)
                {
                    serverInfos.getProtocolClasses().add(Class.forName(pName));
                }
            }
            registerServer(serverId, serverInfos);
        }
        if(clients != null)
        for(String key : clients.keySet())
        {
            String clientId = key;
            Map<String, Object> clientData = (Map<String, Object>) clients.get(clientId);
            ClientInfos clientInfos = new ClientInfos();
            tlsInit(clientInfos, clientData);
            clientInfos.setHost((String) clientData.getOrDefault("host", "127.0.0.1"));
            clientInfos.setPort((Integer) clientData.getOrDefault("port", 50000));
            clientInfos.setProtocolClass(Class.forName((String) clientData.get("protocol")));
            if(clientInfos.getProtocolClass() == null)
                throw new Exception("no protocols for '" + key + "' server...");
            registerClient(clientId, clientInfos);
        }
        if(udpHandlers != null)
        for(String key : udpHandlers.keySet())
        {
            String udpId = key;
            Map<String, Object> udpData = (Map<String, Object>) udpHandlers.get(udpId);
            UdpInfos udpInfos = new UdpInfos();
            udpInfos.setBind((Boolean) udpData.getOrDefault("bind", false));
            udpInfos.setHost((String) udpData.getOrDefault("host", "127.0.0.1"));
            udpInfos.setPort((Integer) udpData.getOrDefault("port", 50000));
            registerUdpHandler(udpId, udpInfos);
        }
        if(mqtt != null)
        for(String key : mqtt.keySet())
        {
            String mqttId = key;
            Map<String, Object> mqttData = (Map<String, Object>) mqtt.get(mqttId);
            MqttInfos infos = new MqttInfos();
            tlsInit(infos, mqttData);
            infos.setBrokerIp((String) mqttData.getOrDefault("broker-ip", null));
            infos.setBrokerPort((int) mqttData.getOrDefault("broker-port", 1883));
            infos.setClientId((String) mqttData.getOrDefault("client-id", "geode-mqtt"));
            infos.setDefaultQos((int) mqttData.getOrDefault("default-qos", 0));
            infos.setTopicsClass(Class.forName((String) mqttData.get("topic-handler")));
            registerMqtt(mqttId, infos);
        }
        logger.info("configuration file process end");
    }

    /**
     * Register server geode.
     *
     * @param id          the id
     * @param serverInfos the server infos
     * @return the geode
     */
    public Geode registerServer(String id, ServerInfos serverInfos)
    {
        serversInfos.put(id, serverInfos);
        logger.info(id + " server registered: " + serverInfos);
        return this;
    }

    public Geode registerMqtt(String id, MqttInfos infos)
    {
        mqttInfos.put(id, infos);
        logger.info(id + " mqtt registered: " + infos);
        return this;
    }

    /**
     * Register client geode.
     *
     * @param id          the id
     * @param clientInfos the client infos
     * @return the geode
     */
    public Geode registerClient(String id, ClientInfos clientInfos)
    {
        clientsInfos.put(id, clientInfos);
        logger.info(id + " client registered: " + clientInfos);
        return this;
    }

    /**
     * Register udp handler geode.
     *
     * @param id       the id
     * @param udpInfos the udp infos
     * @return the geode
     */
    public Geode registerUdpHandler(String id, UdpInfos udpInfos)
    {
        udpsInfos.put(id, udpInfos);
        logger.info(id + " udp handler registered: " + udpInfos);
        return this;
    }

    /**
     * Launch server server.
     *
     * @param id the id
     * @return the server
     */
    public Server launchServer(String id)
    {
        if (!serversInfos.containsKey(id))
        {
            logger.fatal(id + " server not exists");
        } else
        {
            Server server = new Server(serversInfos.get(id));
            server.start();
            servers.add(server);
            return server;
        }
        return null;
    }

    /**
     * Launch client client.
     *
     * @param id the id
     * @return the client
     */
    public Client launchClient(String id)
    {
        if (!clientsInfos.containsKey(id))
        {
            logger.fatal(id + " client not exists");
        } else
        {
            Client tcpClient = new Client(clientsInfos.get(id));
            tcpClient.run();
            tcpClients.add(tcpClient);
            return tcpClient;
        }
        return null;
    }

    /**
     * Launch udp handler udp handler.
     *
     * @param id the id
     * @return the udp handler
     */
    public UdpHandler launchUdpHandler(String id)
    {
        if (!udpsInfos.containsKey(id))
        {
            logger.fatal(id + " udp handler not exists");
        } else
        {
            UdpHandler udpHandler = new UdpHandler(udpsInfos.get(id));
            udpHandler.start();
            udpHandlers.add(udpHandler);
            return udpHandler;
        }
        return null;
    }

    public MqttInstance launchMqtt(String id)
    {
        if (!mqttInfos.containsKey(id))
        {
            logger.fatal(id + " mqtt not exists");
        } else
        {
            MqttInstance mqttInstance = new MqttInstance(mqttInfos.get(id));
            mqttInstance.init();
            mqttInstances.add(mqttInstance);
            return mqttInstance;
        }
        return null;
    }

    /**
     * Is broken boolean.
     *
     * @return the boolean
     */
    public boolean isBroken()
    {
        return broken;
    }
}
