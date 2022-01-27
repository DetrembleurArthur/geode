package com.geode.engine;

import com.geode.engine.graphics.Texture;
import com.geode.engine.system.Application;
import com.geode.engine.system.SceneRef;
import com.geode.engine.system.TextureRef;
import org.apache.logging.log4j.core.config.Configurator;


public class Main extends Application
{

    @SceneRef(initial = true)
    public MainScene mainScene;

    @SceneRef
    public SecondaryScene secondaryScene;

    @TextureRef("assets/icon.png")
    public Texture texture;


    @Override
    public void load()
    {
        getWindow().setTitle("Hello world!");
        getWindow().setSize(1024, 900);
        getWindow().setAspectRatio(16, 9);
        getWindow().center();
    }

    @Override
    protected void freeResources()
    {
        texture.destroy();
    }


    public static void main(String[] args)
    {
        Configurator.initialize(null, "log/log4j.xml");

        Application application = new Main();
        application.run();
    }
}
