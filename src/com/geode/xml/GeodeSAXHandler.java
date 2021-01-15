package com.geode.xml;

import com.geode.log.Log;
import com.geode.net.GeodeIdentifiable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class GeodeSAXHandler extends DefaultHandler implements GeodeIdentifiable
{
    private final XmlNode root;
    private XmlNode current;
    private String xmlFile;

    public GeodeSAXHandler()
    {
        root = new XmlNode();
    }

    public XmlNode getCurrent()
    {
        return current;
    }

    public void setCurrent(XmlNode current)
    {
        this.current = current;
    }

    public String getXmlFile()
    {
        return xmlFile;
    }

    public void setXmlFile(String xmlFile)
    {
        this.xmlFile = xmlFile;
    }

    @Override
    public void startDocument() throws SAXException
    {
        Log.out(this, "start parsing");
    }

    @Override
    public void endDocument() throws SAXException
    {
        Log.out(this, "end parsing");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if(current == null)
        {
            current = root;
        }
        else
        {
            XmlNode temp = new XmlNode();
            temp.setParent(current);
            current.getNodes().add(temp);
            current = temp;
        }
        current.setId(qName);
        for(int i = 0; i < attributes.getLength(); i++)
        {
            current.getProperties().setProperty(attributes.getQName(i), attributes.getValue(i));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if(current.getParent() != null)
            current = current.getParent();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        current.setElement(new String(ch, start, length));
    }

    @Override
    public void warning(SAXParseException e) throws SAXException
    {
        throw e;
    }

    @Override
    public void error(SAXParseException e) throws SAXException
    {
        throw e;
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException
    {
        throw e;
    }

    @Override
    public String getGeodeId()
    {
        return "GeodeSAXHandler::" + xmlFile;
    }

    public XmlNode getRoot()
    {
        return root;
    }
}
