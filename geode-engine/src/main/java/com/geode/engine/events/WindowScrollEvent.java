package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallbackI;

public class WindowScrollEvent extends WindowEvent<WindowScrollI> implements GLFWScrollCallbackI
{
    public WindowScrollEvent(long windowId)
    {
        super(windowId);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetScrollCallback(windowId, this);
    }

    @Override
    public void invoke(long l, double v, double v1)
    {
        getCallbacks().forEach(windowScrollI -> windowScrollI.onScroll((float)v, (float)v1));
    }
}
