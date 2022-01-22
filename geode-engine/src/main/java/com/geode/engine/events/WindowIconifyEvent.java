package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowIconifyCallbackI;

public class WindowIconifyEvent extends WindowEvent<WindowIconifyI> implements GLFWWindowIconifyCallbackI
{
    public WindowIconifyEvent(long windowId)
    {
        super(windowId);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetWindowIconifyCallback(windowId, this);
    }

    @Override
    public void invoke(long l, boolean b)
    {
        getCallbacks().forEach(windowIconifyI -> windowIconifyI.onIconify(b));
    }
}
