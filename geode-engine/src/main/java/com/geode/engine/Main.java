package com.geode.engine;

import com.geode.engine.system.Application;
import com.geode.engine.system.SceneRef;
import org.apache.logging.log4j.core.config.Configurator;


public class Main extends Application
{

    @SceneRef(initial = true)
    public MainScene mainScene;

    @SceneRef
    public SecondaryScene secondaryScene;


    @Override
    public void load()
    {
        getWindow().setTitle("Hello world!");
        getWindow().setSize(1024, 900);
        getWindow().setAspectRatio(16, 9);
        getWindow().center();
    }


    public static void main(String[] args)
    {
        Configurator.initialize(null, "log/log4j.xml");

        Application application = new Main();
        application.run();
    }
}
