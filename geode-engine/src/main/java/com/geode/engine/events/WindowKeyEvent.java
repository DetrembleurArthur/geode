package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

public class WindowKeyEvent extends WindowEvent<WindowKeyI> implements GLFWKeyCallbackI
{
    public WindowKeyEvent(long windowId)
    {
        super(windowId);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetKeyCallback(windowId, this);
    }

    @Override
    public void invoke(long l, int i, int i1, int i2, int i3)
    {
        getCallbacks().forEach(windowKeyI -> windowKeyI.onKey(i, i1, i2, i3));
    }
}
