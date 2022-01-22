package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;

public class WindowCloseEvent extends WindowEvent<WindowCloseI> implements GLFWWindowCloseCallbackI
{
    public WindowCloseEvent(Long id)
    {
        super(id);
    }

    @Override
    public void invoke(long l)
    {
        getCallbacks().forEach(WindowCloseI::onWindowClose);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetWindowCloseCallback(windowId, this);
    }
}
