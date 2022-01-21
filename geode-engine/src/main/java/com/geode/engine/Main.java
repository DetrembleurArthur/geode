package com.geode.engine;

import com.geode.engine.events.WindowEvent;
import com.geode.engine.system.Application;
import com.geode.engine.system.Window;
import com.geode.engine.system.WindowHints;
import com.geode.engine.utils.Colors;
import org.apache.logging.log4j.core.config.Configurator;


public class Main extends Application
{
    @Override
    public void buildWindowHints(WindowHints windowHints)
    {
        windowHints.resizeable(false);
    }

    @WindowEvent.OnClose
    public void onCloseCallback()
    {
        System.out.println("ok");
    }

    @Override
    public void load()
    {
        getWindow().getEventsManager().addOnWindowClose(() -> System.out.println("It works!"));
        getWindow().setSize(500, 500);
        getWindow().setClearColor(Colors.BLUE);
    }

    @Override
    public void update(float dt)
    {

    }

    @Override
    public void draw(Window window)
    {

    }

    @Override
    public void destroy()
    {

    }

    public static void main(String[] args)
    {
        Configurator.initialize(null, "log/log4j.xml");

        Application application = new Main();
        application.run();
    }
}
