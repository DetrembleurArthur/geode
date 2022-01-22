package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowPosCallbackI;

public class WindowPosEvent extends WindowEvent<WindowPosI> implements GLFWWindowPosCallbackI
{
    public WindowPosEvent(long windowId)
    {
        super(windowId);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetWindowPosCallback(windowId, this);
    }

    @Override
    public void invoke(long l, int i, int i1)
    {
        getCallbacks().forEach(windowPosI -> windowPosI.onPos(i, i1));
    }
}
