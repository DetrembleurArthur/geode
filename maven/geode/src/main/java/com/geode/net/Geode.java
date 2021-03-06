package com.geode.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The type Geode.
 */
public final class Geode
{
    private final static Logger logger = LogManager.getLogger(Geode.class);
    private final HashMap<String, ServerInfos> serversInfos;
    private final HashMap<String, ClientInfos> clientsInfos;
    private final HashMap<String, UdpInfos> udpsInfos;
    private final ArrayList<Server> servers;
    private final ArrayList<Client> tcpClients;
    private final ArrayList<UdpHandler> udpHandlers;
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
    }

    public void init(String yamlFilename) throws Exception
    {
        InputStream stream = new FileInputStream(new File(yamlFilename));
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(stream);
        Map<String, Object> servers = (Map<String, Object>) data.get("servers");
        Map<String, Object> clients = (Map<String, Object>) data.get("clients");
        Map<String, Object> udpHandlers = (Map<String, Object>) data.get("udpHandlers");
        if(servers != null)
        for(String key : servers.keySet())
        {
            String serverId = key;
            Map<String, Object> serverData = (Map<String, Object>) servers.get(serverId);
            ServerInfos serverInfos = new ServerInfos();
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
