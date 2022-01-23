package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

public class WindowMouseButtonEvent extends WindowEvent<WindowMouseButtonI> implements GLFWMouseButtonCallbackI
{
    public WindowMouseButtonEvent(long windowId)
    {
        super(windowId);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetMouseButtonCallback(windowId, this);
    }

    @Override
    public void invoke(long l, int i, int i1, int i2)
    {
        getCallbacks().forEach(windowMouseButtonI -> windowMouseButtonI.onMouseButton(i, i1, i2));
    }
}
