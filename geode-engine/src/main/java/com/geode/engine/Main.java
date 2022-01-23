package com.geode.engine;

import com.geode.engine.system.*;
import com.geode.engine.events.WindowEvent;
import com.geode.engine.utils.Colors;
import org.apache.logging.log4j.core.config.Configurator;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;


public class Main extends Application
{
    @Override
    public String buildWindowAttributes(WindowHints windowHints, Vector2i size)
    {
        size.x = 1024;
        size.y = 900;
        return "Demo Application";
    }

    @WindowEvent.OnKey
    public void onKey(int key, int scancode, int action, int mods)
    {
        System.out.println("Key: " + key + " " + scancode + " " + action + " " + mods + " " + KeyManager.getChar(key));
    }

    @WindowEvent.OnTextInput
    public void onKey(char c)
    {
        System.out.println("Text: " + c);
    }

    @WindowEvent.OnMouseButton
    public void f(int btn, int action, int mods)
    {
        //System.out.println("cursor enter: " + btn);
    }

    @Override
    public void load()
    {
        getWindow().getEventsManager().getCloseEvent().getCallbacks().add(() -> System.out.println("It works!"));
        getWindow().setClearColor(Colors.BLUE);
        getWindow().setAspectRatio(16, 9);
        getWindow().center();
        getWindow().requestAttention();
        getWindow().setCursor(Cursor.HAND);
        /*getWindow().setCursor(new Cursor(4, 4, new int[]{
                0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
                0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
                0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
                0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
        }));*/
    }

    @Override
    public void update(float dt)
    {
        if(getKeyManager().isKeyModeRepeated(GLFW.GLFW_KEY_SPACE, KeyManager.Mods.ALT | KeyManager.Mods.CONTROL))
            System.out.println("ok");
        if(getMouseManager().isLeftButtonPressed())
        {
            System.out.println("pressed!");
            getMouseManager().disableLeftButtonAction();
        }
        else if(getMouseManager().isLeftButtonReleased())
        {
            System.out.println("released!");
        }
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
