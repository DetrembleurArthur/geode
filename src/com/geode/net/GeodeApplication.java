package com.geode.net;

import com.geode.exceptions.GeodeException;
import com.geode.log.Log;
import com.geode.xml.GeodeSAXStarter;
import com.geode.xml.XmlNode;

import java.util.concurrent.atomic.AtomicInteger;

public class GeodeApplication implements GeodeIdentifiable
{
    private String xml;
    private XmlNode root;
    private int id;
    private GeodeServerManager geodeServerManager;
    private GeodeClientManager geodeClientManager;
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    public GeodeApplication(String xml) throws GeodeException
    {
        this.xml = xml;
        id = idCounter.incrementAndGet();
        init();
    }

    private void init() throws GeodeException
    {
        root = GeodeSAXStarter.start(xml);
        if(root == null)
            throw new GeodeException(this, xml + " parsing failed");
        geodeServerManager = new GeodeServerManager(this);
        geodeClientManager = new GeodeClientManager(this);
        for(XmlNode node : root.getNodes())
        {
            if(node.getId().equalsIgnoreCase("servers"))
            {
                geodeServerManager.initFromXmlNode(node);
            }
            else if(node.getId().equalsIgnoreCase("clients"))
            {
                geodeClientManager.initFromXmlNode(node);
            }
        }
    }

    public String getXml()
    {
        return xml;
    }

    public XmlNode getRoot()
    {
        return root;
    }

    public int getId()
    {
        return id;
    }

    public GeodeServerManager getGeodeServerManager()
    {
        return geodeServerManager;
    }

    public GeodeClientManager getGeodeClientManager()
    {
        return geodeClientManager;
    }

    public static AtomicInteger getIdCounter()
    {
        return idCounter;
    }

    @Override
    public String getGeodeId()
    {
        return "GeodeApplication::" + id;
    }
}
