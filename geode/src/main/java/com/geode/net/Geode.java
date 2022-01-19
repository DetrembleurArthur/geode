package com.geode.net;

import com.geode.builders.ChannelsManagerInfosBuilder;
import com.geode.builders.ClientInfosBuilder;
import com.geode.builders.MqttInfosBuilder;
import com.geode.builders.ServerInfosBuilder;
import com.geode.builders.SettingsInfosBuilder;
import com.geode.builders.TLSInfosBuilder;
import com.geode.builders.UdpInfosBuilder;
import com.geode.loaders.Loader;
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

    static
    {
        try
        {
            Class.forName("com.geode.builders.ChannelsManagerInfosBuilder");
            Class.forName("com.geode.builders.ClientInfosBuilder");
            Class.forName("com.geode.builders.ServerInfosBuilder");
            Class.forName("com.geode.builders.TLSInfosBuilder");
            Class.forName("com.geode.builders.UdpInfosBuilder");
            Class.forName("com.geode.builders.MqttInfosBuilder");
            Class.forName("com.geode.builders.SettingsInfosBuilder");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

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
            Loader loader = new Loader(SettingsInfosBuilder.create());
            SettingsInfos infos = (SettingsInfos) loader.load(settings).build();
            Logger.setCmdOut(infos.isEnableLogging());
            Logger.setFile(infos.getLoggingFile(), infos.isAppendLogging());
        }
        if(servers != null)
        for(String key : servers.keySet())
        {
            String serverId = key;
            Map<String, Object> serverData = (Map<String, Object>) servers.get(serverId);
            Loader loader = new Loader(ServerInfosBuilder.create());
            ServerInfos infos = (ServerInfos) loader.load(serverData).build();
            registerServer(serverId, infos);
        }
        if(clients != null)
        for(String key : clients.keySet())
        {
            String clientId = key;
            Map<String, Object> clientData = (Map<String, Object>) clients.get(clientId);
            Loader loader = new Loader(ClientInfosBuilder.create());
            ClientInfos infos = (ClientInfos) loader.load(clientData).build();
            registerClient(clientId, infos);
        }
        if(udpHandlers != null)
        for(String key : udpHandlers.keySet())
        {
            String udpId = key;
            Map<String, Object> udpData = (Map<String, Object>) udpHandlers.get(udpId);
            Loader loader = new Loader(UdpInfosBuilder.create());
            UdpInfos infos = (UdpInfos) loader.load(udpData).build();
            registerUdpHandler(udpId, infos);
        }
        if(mqtt != null)
        for(String key : mqtt.keySet())
        {
            String mqttId = key;
            Map<String, Object> mqttData = (Map<String, Object>) mqtt.get(mqttId);
            Loader loader = new Loader(MqttInfosBuilder.create());
            MqttInfos infos = (MqttInfos) loader.load(mqttData).build();
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
