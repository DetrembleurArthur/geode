package com.geode.net;

import com.geode.xml.GeodeXmlHandler;
import com.geode.xml.XmlNode;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public final class Geode
{
    private final static Logger logger = Logger.getLogger(Geode.class);
    private final HashMap<String, ServerInfos> serversInfos;
    private final HashMap<String, ClientInfos> clientsInfos;
    private final HashMap<String, UdpInfos> udpsInfos;
    private final ArrayList<Server> servers;
    private final ArrayList<Client> tcpClients;
    private final ArrayList<UdpHandler> udpHandlers;
    private boolean broken;

    public Geode()
    {
        this("");
    }

    public Geode(String xmlFile)
    {
        broken = false;
        serversInfos = new HashMap<>();
    	clientsInfos = new HashMap<>();
    	udpsInfos = new HashMap<>();
    	servers = new ArrayList<>();
    	tcpClients = new ArrayList<>();
    	udpHandlers = new ArrayList<>();
    	if(!xmlFile.isEmpty())
        {
            loadXml(xmlFile);
        }
    }

    private void loadXml(String filename)
    {
        GeodeXmlHandler geodeXmlHandler = new GeodeXmlHandler();
        geodeXmlHandler.setXmlFile(filename);
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setValidating(true);
        try
        {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(new File(filename), geodeXmlHandler);
            XmlNode node = geodeXmlHandler.getRoot();
            if(node.getId().equalsIgnoreCase("geode"))
            {
                for(XmlNode xmlNode : node.getNodes())
                {
                    if(xmlNode.getId().equalsIgnoreCase("servers"))
                    {
                        for(XmlNode serverNode : xmlNode.getNodes())
                        {
                            if(serverNode.getId().equalsIgnoreCase("server"))
                            {
                                ServerInfos serverInfos = new ServerInfos();
                                serverInfos.setHost(serverNode.getProperties().getProperty("ip"));
                                serverInfos.setPort(Integer.parseInt(serverNode.getProperties().getProperty("port")));
                                serverInfos.setBacklog(Integer.parseInt(serverNode.getProperties().getProperty("backlog")));
                                for(XmlNode serverElementNode : serverNode.getNodes())
                                {
                                    if(serverElementNode.getId().equalsIgnoreCase("protocols"))
                                    {
                                        ArrayList<Class<?>> protocolClasses = new ArrayList<>();
                                        serverInfos.setProtocolClasses(protocolClasses);
                                        for(XmlNode protocolNode : serverElementNode.getNodes())
                                        {
                                            if(protocolNode.getId().equalsIgnoreCase("protocol"))
                                            {
                                                protocolClasses.add(Class.forName(protocolNode.getProperties().getProperty("class")));
                                            }
                                        }
                                    }
                                }
                                registerServer(serverNode.getProperties().getProperty("id"), serverInfos);
                            }
                        }
                    }
                    else if(xmlNode.getId().equalsIgnoreCase("clients"))
                    {
                        for(XmlNode clientNode : xmlNode.getNodes())
                        {
                            if(clientNode.getId().equalsIgnoreCase("client"))
                            {
                                ClientInfos clientInfos = new ClientInfos();
                                clientInfos.setHost(clientNode.getProperties().getProperty("server-ip"));
                                clientInfos.setPort(Integer.parseInt(clientNode.getProperties().getProperty("server-port")));
                                clientInfos.setProtocolClass(Class.forName(clientNode.getProperties().getProperty("protocol")));
                                registerClient(clientNode.getProperties().getProperty("id"), clientInfos);
                            }
                        }
                    }
                    else if(xmlNode.getId().equalsIgnoreCase("udp-handlers"))
                    {
                        for(XmlNode udpNode : xmlNode.getNodes())
                        {
                            if(udpNode.getId().equalsIgnoreCase("udp-handler"))
                            {
                                UdpInfos udpInfos = new UdpInfos();
                                udpInfos.setHost(udpNode.getProperties().getProperty("host"));
                                udpInfos.setPort(Integer.parseInt(udpNode.getProperties().getProperty("port")));
                                udpInfos.setBind(Boolean.parseBoolean(udpNode.getProperties().getProperty("bind")));
                                registerUdpHandler(udpNode.getProperties().getProperty("id"), udpInfos);
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | ClassNotFoundException e)
        {
            broken = true;
            logger.fatal("xml parsing error: " + e.getMessage());
        }
    }
    
    public Geode registerServer(String id, ServerInfos serverInfos)
    {
    	serversInfos.put(id, serverInfos);
    	logger.info(id + " server registered: " + serverInfos);
    	return this;
    }
    
    public Geode registerClient(String id, ClientInfos clientInfos)
    {
        clientsInfos.put(id, clientInfos);
        logger.info(id + " client registered: " + clientInfos);
        return this;
    }

    public Geode registerUdpHandler(String id, UdpInfos udpInfos)
    {
        udpsInfos.put(id, udpInfos);
        logger.info(id + " udp handler registered: " + udpInfos);
        return this;
    }

    public Server launchServer(String id)
    {
        if(!serversInfos.containsKey(id))
        {
            logger.fatal(id + " server not exists");
        }
        else
        {
            Server server = new Server(serversInfos.get(id));
            server.start();
            servers.add(server);
            return server;
        }
        return null;
    }

    public Client launchClient(String id)
    {
        if(!clientsInfos.containsKey(id))
        {
            logger.fatal(id + " client not exists");
        }
        else
        {
            Client tcpClient = new Client(clientsInfos.get(id));
            tcpClient.run();
            tcpClients.add(tcpClient);
            return tcpClient;
        }
        return null;
    }

    public UdpHandler launchUdpHandler(String id)
    {
        if(!udpsInfos.containsKey(id))
        {
            logger.fatal(id + " udp handler not exists");
        }
        else
        {
            UdpHandler udpHandler = new UdpHandler(udpsInfos.get(id));
            udpHandler.start();
            udpHandlers.add(udpHandler);
            return udpHandler;
        }
        return null;
    }

    public boolean isBroken()
    {
        return broken;
    }
}
