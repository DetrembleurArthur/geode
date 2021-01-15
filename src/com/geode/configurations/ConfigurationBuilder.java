package com.geode.configurations;

import com.geode.annotations.Protocol;
import com.geode.xml.XmlNode;

import java.util.ArrayList;

public class ConfigurationBuilder
{
    public static ServerConfigurations buildServerConfiguration(XmlNode node) throws Exception
    {
        ServerConfigurations configurations = null;
        if(node.getId().equalsIgnoreCase("server"))
        {
            configurations = new ServerConfigurations();
            configurations.setIp(node.getProperties().getProperty("ip"));
            configurations.setPort(Integer.parseInt(node.getProperties().getProperty("port")));
            for(XmlNode sub : node.getNodes())
            {
                if(sub.getId().equalsIgnoreCase("protocols"))
                {
                    configurations.getProtocolConfigurations().addAll(buildProtocolConfigurationsList(sub));
                }
            }
        }
        return configurations;
    }

    public static ClientConfigurations buildClientConfiguration(XmlNode node) throws Exception
    {
        ClientConfigurations configurations = null;
        if(node.getId().equalsIgnoreCase("client"))
        {
            configurations = new ClientConfigurations();
            configurations.setServerIp(node.getProperties().getProperty("server-ip"));
            configurations.setServerPort(Integer.parseInt(node.getProperties().getProperty("server-port")));
            for(XmlNode sub : node.getNodes())
            {
                if(sub.getId().equalsIgnoreCase("protocols"))
                {
                    configurations.getProtocolConfigurations().addAll(buildProtocolConfigurationsList(sub));
                }
            }
        }
        return configurations;
    }

    public static ProtocolConfigurations buildProtocolConfigurations(XmlNode node) throws Exception
    {
        ProtocolConfigurations configurations = null;
        if(node.getId().equalsIgnoreCase("protocol"))
        {
            configurations = new ProtocolConfigurations();
            Class<?> protocolClass = Class.forName(node.getProperties().getProperty("class"));
            if(!protocolClass.isAnnotationPresent(Protocol.class))
                throw new Exception(protocolClass.getName() + " is not marked as 'Protocol'");
            configurations.setProtocolClass(protocolClass);
            configurations.setName((String) node.getProperties().getOrDefault("name", protocolClass.getAnnotation(Protocol.class).name()));
        }
        return configurations;
    }

    public static ArrayList<ProtocolConfigurations> buildProtocolConfigurationsList(XmlNode node) throws Exception
    {
        ArrayList<ProtocolConfigurations> protocolConfigurations = new ArrayList<>();
        if(node.getId().equalsIgnoreCase("protocols"))
        {
            for(XmlNode sub : node.getNodes())
            {
                protocolConfigurations.add(buildProtocolConfigurations(sub));
            }
        }
        return protocolConfigurations;
    }
}
