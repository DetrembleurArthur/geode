package com.geode.engine;

import com.geode.engine.events.WindowEvent;
import com.geode.engine.system.Application;
import com.geode.engine.system.Monitor;
import com.geode.engine.system.Window;
import com.geode.engine.system.WindowHints;
import com.geode.engine.utils.Colors;
import org.apache.logging.log4j.core.config.Configurator;
import org.joml.Vector2i;


public class Main extends Application
{
    @Override
    public String buildWindowAttributes(WindowHints windowHints, Vector2i size)
    {
        size.x = 1024;
        size.y = 900;
        return "Demo Application";
    }

    @WindowEvent.OnClose
    public void onCloseCallback()
    {
        System.out.println("ok");
    }

    @WindowEvent.OnSize
    public void onSizeCallback(int width, int height)
    {
        System.out.println("New size: " + width + " " + height);
    }

    @WindowEvent.OnFramebufferSize
    public void onFramebufferSizeCallback(int width, int height)
    {
        System.out.println("New FB size: " + width + " " + height);
    }

    @WindowEvent.OnContentScale
    public void onContentScaleCallback(float xs, float xy)
    {
        System.out.println("New content scale: " + xs + " " + xy);
    }

    @WindowEvent.OnPos
    public void onPosCallback(float x, float y)
    {
        //System.out.println("New position: " + x + " " + y);
    }

    @WindowEvent.OnIconify
    public void onIconifyCallback(boolean state)
    {
        System.out.println((state ? "iconified" : "restored => ") + getWindow().isIconified());
    }

    @Override
    public void load()
    {
        getWindow().getEventsManager().getCloseEvent().getCallbacks().add(() -> System.out.println("It works!"));
        getWindow().setClearColor(Colors.BLUE);
        getWindow().setAspectRatio(16, 9);
        getWindow().center();

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
