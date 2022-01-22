package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;

public class WindowSizeEvent extends WindowEvent<WindowSizeI> implements GLFWWindowSizeCallbackI
{
    public WindowSizeEvent(Long id)
    {
        super(id);
    }

    @Override
    public void invoke(long l, int i, int i1)
    {
        getCallbacks().forEach(windowSizeI -> windowSizeI.onWindowSize(i, i1));
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetWindowSizeCallback(windowId, this);
    }
}
