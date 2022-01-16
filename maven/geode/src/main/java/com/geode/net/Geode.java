package com.geode.net;

import com.geode.logging.Logger;
import com.geode.net.channels.ChannelsManager;
import com.geode.net.channels.ChannelsManagerInfos;
import com.geode.net.mqtt.MqttInfos;
import com.geode.net.mqtt.MqttInstance;
import com.geode.net.tls.TLSInfos;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Geode.
 */
public final class Geode
{
    private static final Logger logger = new Logger(Geode.class);

    private final HashMap<String, ServerInfos> serversInfos;
    private final HashMap<String, ClientInfos> clientsInfos;
    private final HashMap<String, ClientInfos> lightClientsInfos;
    private final HashMap<String, UdpInfos> udpsInfos;
    private final HashMap<String, MqttInfos> mqttInfos;
    private final ArrayList<Server> servers;
    private final ArrayList<Client> tcpClients;
    private final ArrayList<UdpHandler> udpHandlers;
    private final ArrayList<MqttInstance> mqttInstances;
    private final ArrayList<LightClient> tcpLightClients;
    private boolean broken;
    private ChannelsManager channelsManager;

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
        lightClientsInfos = new HashMap<>();
        tcpLightClients = new ArrayList<>();
        channelsManager = new ChannelsManager();
        channelsManager.createChannel("geode").set(this);
    }

    public static Geode load(String filename) throws Exception
    {
        Geode geode = new Geode();
        geode.init(filename);
        return geode;
    }

    public static Geode load() throws Exception
    {
        return load("src/main/resources/geode.yaml");
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
                infos.setKeystorePassword((String)data.getOrDefault("kspswd", null));
                infos.setKeystoreKeyPassword((String)data.getOrDefault("kskeypswd", null));
            }
            else
            {
                infos.setCafile((String)data.getOrDefault("cafile", null));
                infos.setCertfile((String)data.getOrDefault("certfile", null));
                infos.setKeyfile((String)data.getOrDefault("keyfile", null));
            }
        }
    }

    private ChannelsManagerInfos channelsManagerInfosInit(Map<String, Object> data)
    {
        if(data.containsKey("channels-manager"))
        {
            ChannelsManagerInfos infos = new ChannelsManagerInfos();
            data = (Map<String, Object>) data.get("channels-manager");
            infos.setEnable((boolean) data.getOrDefault("enable", true));
            infos.setStrict((boolean) data.getOrDefault("strict", true));
            return infos;
        }
        return new ChannelsManagerInfos();
    }

    public void init(String yamlFilename) throws Exception
    {
        logger.info("load configuration file : " + yamlFilename);
        InputStream stream = new FileInputStream(new File(yamlFilename));
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(stream);
        logger.info("data loaded : " + data);

        Map<String, Object> settings = (Map<String, Object>) data.get("settings");
        Map<String, Object> servers = (Map<String, Object>) data.get("servers");
        Map<String, Object> clients = (Map<String, Object>) data.get("clients");
        Map<String, Object> udpHandlers = (Map<String, Object>) data.get("udpHandlers");
        Map<String, Object> mqtt = (Map<String, Object>) data.get("mqtt");
        if(settings != null)
        {
            boolean logging = (boolean) settings.getOrDefault("enable-logging", true);
            Logger.setCmdOut(logging);
            String loggingPath = (String) settings.getOrDefault("logging-file", null);
            boolean loggingFileAppend = (boolean) settings.getOrDefault("logging-file-append", true);
            Logger.setFile(loggingPath, loggingFileAppend);
        }
        if(servers != null)
        for(String key : servers.keySet())
        {
            String serverId = key;
            Map<String, Object> serverData = (Map<String, Object>) servers.get(serverId);
            ServerInfos serverInfos = new ServerInfos();
            serverInfos.setName(serverId);
            tlsInit(serverInfos, serverData);
            serverInfos.setChannelsManagerInfos(channelsManagerInfosInit(serverData));
            serverInfos.setHost((String) serverData.getOrDefault("host", "127.0.0.1"));
            serverInfos.setPort((Integer) serverData.getOrDefault("port", 50000));
            serverInfos.setBacklog((Integer) serverData.getOrDefault("backlog", 10));
            serverInfos.setMaxHandlers((Integer) serverData.getOrDefault("max-handlers", Integer.MAX_VALUE));
            serverInfos.setEnableDiscovery((Boolean) serverData.getOrDefault("discovery", true));
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
            clientInfos.setName(clientId);
            tlsInit(clientInfos, clientData);
            clientInfos.setChannelsManagerInfos(channelsManagerInfosInit(clientData));
            clientInfos.setHost((String) clientData.getOrDefault("host", "127.0.0.1"));
            clientInfos.setPort((Integer) clientData.getOrDefault("port", 50000));
            if((Boolean)clientData.getOrDefault("light", false))
            {
                clientInfos.setEnableDiscovery(false);
                registerLightClient(clientId, clientInfos);
            }
            else
            {
                clientInfos.setEnableDiscovery((Boolean) clientData.getOrDefault("discovery", true));
                clientInfos.setProtocolClass(Class.forName((String) clientData.get("protocol")));
                if(clientInfos.getProtocolClass() == null)
                    throw new Exception("no protocols for '" + key + "' client...");
                registerClient(clientId, clientInfos);
            }
        }
        if(udpHandlers != null)
        for(String key : udpHandlers.keySet())
        {
            String udpId = key;
            Map<String, Object> udpData = (Map<String, Object>) udpHandlers.get(udpId);
            UdpInfos udpInfos = new UdpInfos();
            udpInfos.setName(udpId);
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
            infos.setName(mqttId);
            tlsInit(infos, mqttData);
            infos.setProfile((String) mqttData.getOrDefault("profile", "pub-sub"));
            infos.setBrokerIp((String) mqttData.getOrDefault("broker-ip", null));
            infos.setBrokerPort((int) mqttData.getOrDefault("broker-port", 1883));
            infos.setClientId((String) mqttData.getOrDefault("client-id", "geode-mqtt"));
            infos.setDefaultQos((int) mqttData.getOrDefault("default-qos", 0));
            if(mqttData.containsKey("topic-handler"))
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
        serverInfos.setChannelsManager(channelsManager);
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
        clientInfos.setChannelsManager(channelsManager);
        clientsInfos.put(id, clientInfos);
        logger.info(id + " light client registered: " + clientInfos);
        return this;
    }

    public Geode registerLightClient(String id, ClientInfos clientInfos)
    {
        lightClientsInfos.put(id, clientInfos);
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

    public void launchClosableServer(String id)
    {
        Server server = launchServer(id);
        JOptionPane.showMessageDialog(null, "Click here to close '" + id + "' server");
        server.end();
    }

    /**
     * Launch client client.
     *
     * @param id the id
     * @return the client
     */
    public LightClient launchLightClient(String id)
    {
        if (!lightClientsInfos.containsKey(id))
        {
            logger.fatal(id + " light client not exists");
        } else
        {
            LightClient tcpClient = new LightClient(lightClientsInfos.get(id));
            tcpClient.init();
            tcpLightClients.add(tcpClient);
            return tcpClient;
        }
        return null;
    }

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

    public ChannelsManager getChannelsManager()
    {
        return channelsManager;
    }
}
