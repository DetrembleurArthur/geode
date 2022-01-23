package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;

public class WindowFocusEvent extends WindowEvent<WindowFocusI> implements GLFWWindowFocusCallbackI
{
    public WindowFocusEvent(long windowId)
    {
        super(windowId);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetWindowFocusCallback(windowId, this);
    }

    @Override
    public void invoke(long l, boolean b)
    {
        getCallbacks().forEach(windowFocusI -> windowFocusI.onFocus(b));
    }
}
