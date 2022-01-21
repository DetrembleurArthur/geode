package com.geode.engine.events;

import com.geode.engine.system.Window;
import lombok.Getter;
import lombok.SneakyThrows;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class WindowEventsManager
{
    private final HashMap<Class<?>, WindowEvent<?>> eventsMap;
    @Getter
    private final Window window;

    public WindowEventsManager(Window window)
    {
        this.window = window;
        eventsMap = new HashMap<>();
        setOnWindowClose();
    }

    public void initObject(Object obj)
    {
        for(Method method : obj.getClass().getMethods())
        {
            if(method.isAnnotationPresent(WindowEvent.OnClose.class))
            {
                window.getEventsManager().addOnWindowClose(() -> {
                    try
                    {
                        method.invoke(obj);
                    } catch (IllegalAccessException | InvocationTargetException e)
                    {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private WindowEvent<?> initEvent(Class<?> eventClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
    {
        WindowEvent<?> windowEvent = (WindowEvent<?>) eventClass.getConstructor().newInstance();
        eventsMap.put(eventClass, windowEvent);
        return windowEvent;
    }

    private void setCallback(Class<?> eventClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        GLFW.glfwSetWindowCloseCallback(window.getId(), (WindowCloseEvent) initEvent(eventClass));
    }

    @SneakyThrows
    public void setOnWindowClose()
    {
        setCallback(WindowCloseEvent.class);
    }

    public void addOnWindowClose(WindowCloseI windowCloseI)
    {
        WindowCloseEvent windowCloseEvent = (WindowCloseEvent) eventsMap.get(WindowCloseEvent.class);
        windowCloseEvent.getCallbacks().add(windowCloseI);
    }

    public void removeOnWindowClose(WindowCloseI windowCloseI)
    {
        WindowCloseEvent windowCloseEvent = (WindowCloseEvent) eventsMap.get(WindowCloseEvent.class);
        if(windowCloseI != null)
            windowCloseEvent.getCallbacks().remove(windowCloseI);
        else
            windowCloseEvent.getCallbacks().clear();
    }
}
