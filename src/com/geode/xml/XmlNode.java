package com.geode.xml;

import java.util.ArrayList;
import java.util.Properties;

public class XmlNode
{
    private XmlNode parent;
    private String id;
    private Properties properties = new Properties();
    private ArrayList<XmlNode> nodes = new ArrayList<>();
    private String element;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Properties getProperties()
    {
        return properties;
    }

    public void setProperties(Properties properties)
    {
        this.properties = properties;
    }

    public ArrayList<XmlNode> getNodes()
    {
        return nodes;
    }

    public void setNodes(ArrayList<XmlNode> nodes)
    {
        this.nodes = nodes;
    }

    public XmlNode getParent()
    {
        return parent;
    }

    public void setParent(XmlNode parent)
    {
        this.parent = parent;
    }

    public String getElement()
    {
        return element;
    }

    public void setElement(String element)
    {
        this.element = element;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ID: ").append(getId()).append(" ");
        for(Object key : properties.keySet())
            builder.append(key).append("=").append(properties.getProperty((String) key)).append(" ");
        for(XmlNode node : nodes)
            builder.append("\n").append(node);
        if(element != null)
            builder.append("DATA: ").append(element);
        return builder.toString();
    }
}
