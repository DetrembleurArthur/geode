package com.geode.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Main
{
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args)
    {
        Configurator.initialize(null, "log/log4j.xml");
        logger.info("Hello world!");
    }
}
