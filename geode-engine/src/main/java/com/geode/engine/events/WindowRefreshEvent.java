package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowRefreshCallbackI;

public class WindowRefreshEvent extends WindowEvent<WindowRefreshI> implements GLFWWindowRefreshCallbackI
{
    public WindowRefreshEvent(long windowId)
    {
        super(windowId);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetWindowRefreshCallback(windowId, this);
    }

    @Override
    public void invoke(long l)
    {
        getCallbacks().forEach(WindowRefreshI::onRefresh);
    }
}
