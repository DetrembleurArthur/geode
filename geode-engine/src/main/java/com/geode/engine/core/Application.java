package com.geode.engine.core;

import com.geode.engine.conf.Configurations;
import com.geode.engine.dispatchers.ResourcesDispatcher;
import com.geode.engine.exceptions.WindowException;
import com.geode.engine.graphics.Shader;
import lombok.Getter;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13C.GL_MULTISAMPLE;


public abstract class Application implements Manageable
{
    @Getter
    private static Application application;

    @Getter
    private Window window;

    @Getter
    private KeyManager keyManager;

    @Getter
    private MouseManager mouseManager;

    @Getter
    private Scene<?> scene;

    @Getter
    private ResourcesDispatcher resourcesDispatcher;

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

    private void initPrimariesEventManagers()
    {
        window.getEventsManager().clearAllEvents();
        keyManager = KeyManager.get();
        KeyManager.setLockKeysMode(true);
        mouseManager = MouseManager.get();
        window.getEventsManager().getKeyEvent().getCallbacks().add(keyManager);
        window.getEventsManager().getMouseButtonEvent().getCallbacks().add(mouseManager);
        window.getEventsManager().getSizeEvent().getCallbacks().add(new ViewportManager());
    }

    private void initDependencyInjections() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
    {
        resourcesDispatcher = new ResourcesDispatcher();
        resourcesDispatcher.dispatch(this);
        Scene<?> init = null;
        for (Field field : getClass().getFields())
        {
            if (field.isAnnotationPresent(SceneRef.class))
            {
                SceneRef sceneRef = field.getAnnotation(SceneRef.class);
                if (Scene.class.isAssignableFrom(field.getType()))
                {
                    Scene<Application> scene = (Scene<Application>) field.getType().getConstructor().newInstance();
                    scene.setKeepState(sceneRef.keep());
                    scene.setParent(this);
                    field.set(this, scene);
                    if (sceneRef.initial())
                        init = scene;
                }
            }
        }
        if (init != null)
            setScene(init);
    }

    private void init()
    {
        Application.setApplication(this);
        try
        {
            Configurations.load();
            WindowHints windowHints = WindowHints.create();
            Vector2i winSize = Window.DEFAULT_SIZE;
            String title = buildWindowAttributes(windowHints, winSize);
            window = Window.get(winSize, title, windowHints);
            initPrimariesEventManagers();

            window.makeCurrent();
            GL.createCapabilities();

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_MULTISAMPLE);

            window.applyViewport(0, 0, winSize.x, winSize.y);

            initDependencyInjections();
        } catch (WindowException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void run()
    {
        window.show();
        load();
        float dt = 0;
        float lastFrame = 0;
        while (!window.shouldClose())
        {
            float frameTime = Time.getTime();
            dt = frameTime - lastFrame;
            Time.setDt(dt);
            lastFrame = frameTime;
            window.checkEvents();
            window.clear();
            update(dt);
            window.flip();

        }
        destroy();
        window.close();
    }

    public int getFps()
    {
        return (int) (1 / Time.getDt());
    }

    public void setScene(Scene<?> scene)
    {
        if (this.scene != null)
        {
            this.scene.disactive();
        }
        this.scene = scene;
        initPrimariesEventManagers();
        window.getEventsManager().initObject(this.scene);
        this.scene.active();
    }

    @Override
    public final void update(float dt)
    {
        if (scene != null)
            scene.update(dt);
    }

    @Override
    public final void destroy()
    {
        if (scene != null)
        {
            scene.destroy();
        }
        freeResources();
        Shader.destroyDefault();
    }

    private void freeResources()
    {
        resourcesDispatcher.destroy(this);
    }
}
