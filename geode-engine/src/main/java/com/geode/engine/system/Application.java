package com.geode.engine.system;

import com.geode.engine.exceptions.WindowException;
import com.geode.engine.utils.Colors;
import lombok.Getter;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13C.GL_MULTISAMPLE;


public abstract class Application implements Scene
{
    @Getter
    private static Application application;

    @Getter
    private Window window;

    public static void setApplication(Application application)
    {
        Application.application = application;
    }

    public Application()
    {
        init();
    }

    public String buildWindowAttributes(WindowHints windowHints, Vector2i size)
    {
        // to implement if needed
        return Window.DEFAULT_TITLE;
    }

    private void init()
    {
        try
        {
            WindowHints windowHints = WindowHints.create();
            Vector2i winSize = Window.DEFAULT_SIZE;
            String title = buildWindowAttributes(windowHints, winSize);
            window = Window.create(winSize, title, windowHints);
            window.getEventsManager().initObject(this);
            window.makeCurrent();
            GL.createCapabilities();

            window.setClearColor(Colors.GREEN);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_MULTISAMPLE);
            Application.setApplication(this);
        } catch (WindowException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void run()
    {
        window.show();
        load();
        while (!window.shouldClose())
        {
            window.pollEvents();
            window.clear();
            update(0f);
            draw(window);
            window.flip();
        }
        destroy();
        window.close();
    }
}
