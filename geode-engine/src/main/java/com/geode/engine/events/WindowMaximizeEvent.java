package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowMaximizeCallbackI;

public class WindowMaximizeEvent extends WindowEvent<WindowMaximizeI> implements GLFWWindowMaximizeCallbackI
{
    public WindowMaximizeEvent(long windowId)
    {
        super(windowId);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetWindowMaximizeCallback(windowId, this);
    }

    @Override
    public void invoke(long l, boolean b)
    {
        getCallbacks().forEach(windowMaximizeI -> windowMaximizeI.onMaximize(b));
    }
}
