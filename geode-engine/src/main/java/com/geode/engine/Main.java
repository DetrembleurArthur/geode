package com.geode.engine;

import com.geode.engine.graphics.Texture;
import com.geode.engine.system.Application;
import com.geode.engine.system.SceneRef;
import com.geode.engine.system.TextureRef;
import com.geode.engine.system.WindowHints;
import org.apache.logging.log4j.core.config.Configurator;
import org.joml.Vector2i;


public class Main extends Application
{

    @SceneRef(initial = true)
    public MainScene mainScene;

    @SceneRef
    public SecondaryScene secondaryScene;

    @TextureRef("assets/img.png")
    public Texture texture;

    @Override
    public void load()
    {
        getWindow().setTitle("Hello world!");
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