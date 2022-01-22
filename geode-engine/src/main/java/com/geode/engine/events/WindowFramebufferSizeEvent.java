package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;

public class WindowFramebufferSizeEvent extends WindowEvent<WindowframebufferSizeI> implements GLFWFramebufferSizeCallbackI
{
    public WindowFramebufferSizeEvent(Long id)
    {
        super(id);
    }

    @Override
    public void invoke(long l, int i, int i1)
    {
        getCallbacks().forEach(windowSizeI -> windowSizeI.onWindowFramebufferSize(i, i1));
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetFramebufferSizeCallback(windowId, this);
    }
}
