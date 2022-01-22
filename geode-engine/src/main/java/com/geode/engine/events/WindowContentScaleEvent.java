package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowContentScaleCallbackI;

public class WindowContentScaleEvent extends WindowEvent<WindowContentScaleI> implements GLFWWindowContentScaleCallbackI
{
    public WindowContentScaleEvent(long windowId)
    {
        super(windowId);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetWindowContentScaleCallback(windowId, this);
    }

    @Override
    public void invoke(long l, float v, float v1)
    {
        getCallbacks().forEach(windowContentScaleI -> windowContentScaleI.onContentScale(v, v1));
    }
}
