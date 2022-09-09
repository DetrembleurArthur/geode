package com.geode.engine.conf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Configurations
{
    private static Logger logger = LogManager.getLogger(Configurations.class);

    private static Properties properties;

    public static String assetsPath;
    public static String imageSubPath;
    public static String shadersSubPath;

    public static void load()
    {
        properties = new Properties();
        try
        {
            properties.load(new FileReader("geode-engine.properties"));
            logger.info("'geode-engine.properties' loaded");
        } catch (IOException e)
        {
            logger.warn("'geode-engine.properties' can not be load");
        }
        finally
        {
            assetsPath = properties.getProperty("assets-dir", "assets/");
            imageSubPath = properties.getProperty("images-dir", "images/");
            shadersSubPath = properties.getProperty("shaders-dir", "shaders/");
            logger.info("default configuration loaded");
        }
    }
}
