package com.geode.xml;

import com.geode.log.Log;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class GeodeSAXStarter
{
    public static XmlNode start(String xmlFilename)
    {
        GeodeSAXHandler geodeSAXHandler = new GeodeSAXHandler();
        geodeSAXHandler.setXmlFile(xmlFilename);
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setValidating(true);
        try
        {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(new File(xmlFilename), geodeSAXHandler);
        } catch (ParserConfigurationException | SAXException | IOException e)
        {
            Log.err(geodeSAXHandler, e.getMessage());
            return null;
        }
        return geodeSAXHandler.getRoot();
    }
}
