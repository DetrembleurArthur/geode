package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallbackI;

public class WindowCursorEnterEvent extends WindowEvent<WindowCursorEnterI> implements GLFWCursorEnterCallbackI
{
    public WindowCursorEnterEvent(long windowId)
    {
        super(windowId);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetCursorEnterCallback(windowId, this);
    }

    @Override
    public void invoke(long l, boolean b)
    {
        getCallbacks().forEach(windowCursorEnterI -> windowCursorEnterI.onEnter(b));
    }
}
