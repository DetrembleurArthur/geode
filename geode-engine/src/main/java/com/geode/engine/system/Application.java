package com.geode.engine.system;

import com.geode.engine.exceptions.WindowException;
import com.geode.engine.graphics.Shader;
import com.geode.engine.graphics.Texture;
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
    private Scene<?> currentScene;

    @Getter
    private float beginTime;

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
        Scene<?> init = null;
        for(Field field : getClass().getFields())
        {
            if(field.isAnnotationPresent(SceneRef.class))
            {
                SceneRef sceneRef = field.getAnnotation(SceneRef.class);
                if(Scene.class.isAssignableFrom(field.getType()))
                {
                    Scene<Application> scene = (Scene<Application>) field.getType().getConstructor().newInstance();
                    scene.setParent(this);
                    field.set(this, scene);
                    if(sceneRef.initial())
                        init = scene;
                }
            }
            else if(field.isAnnotationPresent(TextureRef.class))
            {
                TextureRef textureRef = field.getAnnotation(TextureRef.class);
                if(Texture.class.isAssignableFrom(field.getType()))
                {
                    Texture texture = new Texture(textureRef.value());
                    field.set(this, texture);
                }
            }
        }
        if(init != null)
            setScene(init);
    }

    private void init()
    {
        try
        {
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

            beginTime = Time.getTime();
            initDependencyInjections();
            Application.setApplication(this);
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
        float frameTime = Time.getTime();
        float dt;
        while (!window.shouldClose())
        {
            dt = Time.getTime() - frameTime;
            Time.setDt(dt);
            frameTime = Time.getTime();
            window.checkEvents();
            window.clear();
            update(dt);
            draw(window);
            window.flip();
        }
        destroy();
        window.close();
    }

    public void setScene(Scene<?> scene)
    {
        if(currentScene != null)
        {
            currentScene.disactive();
        }
        currentScene = scene;
        initPrimariesEventManagers();
        window.getEventsManager().initObject(currentScene);
        currentScene.active();
    }

    @Override
    public final void update(float dt)
    {
        if(currentScene != null)
            currentScene.update(dt);
    }

    @Override
    public final void draw(Window window)
    {
        if(currentScene != null)
            currentScene.draw(window);
    }

    @Override
    public final void destroy()
    {
        if(currentScene != null)
            currentScene.destroy();
        freeResources();
        Shader.destroyDefault();
    }

    protected abstract void freeResources();
}
