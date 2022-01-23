package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class WindowCursorPosEvent extends WindowEvent<WindowCursorPosI> implements GLFWCursorPosCallbackI
{
    public WindowCursorPosEvent(long windowId)
    {
        super(windowId);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetCursorPosCallback(windowId, this);
    }

    @Override
    public void invoke(long l, double v, double v1)
    {
        getCallbacks().forEach(windowCursorPosI -> windowCursorPosI.onCursorPos(v, v1));
    }
}
