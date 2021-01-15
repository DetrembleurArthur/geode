package com.geode.net;

import com.geode.exceptions.GeodeException;
import com.geode.xml.XmlNode;

public abstract class GeodeManager
{
    protected final GeodeApplication geodeApplication;

    public GeodeManager(GeodeApplication geodeApplication)
    {
        this.geodeApplication = geodeApplication;
    }

    abstract void initFromXmlNode(XmlNode node) throws GeodeException;

    public GeodeApplication getGeodeApplication()
    {
        return geodeApplication;
    }
}
