package com.geode.engine;

import com.geode.engine.core.Application;
import com.geode.engine.core.SceneRef;
import com.geode.engine.core.WindowHints;
import org.apache.logging.log4j.core.config.Configurator;
import org.joml.Vector2i;


public class Main extends Application
{

    @SceneRef(initial = true)
    public MainScene mainScene;


    @Override
    public String buildWindowAttributes(WindowHints windowHints, Vector2i size)
    {
        return super.buildWindowAttributes(windowHints, size);
    }

    @Override
    public void load()
    {
        getWindow().setTitle("Hello world!");
        getWindow().center();
    }


    public static void main(String[] args)
    {
        Configurator.initialize(null, "log/log4j.xml");

        Application application = new Main();
        application.run();
    }
}
