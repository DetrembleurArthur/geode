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
    @Getter
    private final WindowCloseEvent closeEvent;

    @Getter
    private final WindowSizeEvent sizeEvent;

    @Getter
    private final WindowFramebufferSizeEvent framebufferSizeEvent;

    @Getter
    private final WindowContentScaleEvent contentScaleEvent;

    @Getter
    private final WindowPosEvent posEvent;

    @Getter
    private final WindowIconifyEvent iconifyEvent;

    @Getter
    private final Window window;

    public WindowEventsManager(Window window)
    {
        this.window = window;
        closeEvent = new WindowCloseEvent(window.getId());
        sizeEvent = new WindowSizeEvent(window.getId());
        framebufferSizeEvent = new WindowFramebufferSizeEvent(window.getId());
        contentScaleEvent = new WindowContentScaleEvent(window.getId());
        posEvent = new WindowPosEvent(window.getId());
        iconifyEvent = new WindowIconifyEvent(window.getId());
    }

    public void initObject(Object obj)
    {
        for(Method method : obj.getClass().getMethods())
        {
            if(method.isAnnotationPresent(WindowEvent.OnClose.class))
            {
                closeEvent.getCallbacks().add(() -> {try {method.invoke(obj);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnSize.class))
            {
                sizeEvent.getCallbacks().add((w, h) -> {try {method.invoke(obj, w, h);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnFramebufferSize.class))
            {
                framebufferSizeEvent.getCallbacks().add((w, h) -> {try {method.invoke(obj, w, h);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnContentScale.class))
            {
                contentScaleEvent.getCallbacks().add((xs, xy) -> {try {method.invoke(obj, xs, xy);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnPos.class))
            {
                posEvent.getCallbacks().add((x, y) -> {try {method.invoke(obj, x, y);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnIconify.class))
            {
                iconifyEvent.getCallbacks().add((ic) -> {try {method.invoke(obj, ic);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
        }
    }
}
