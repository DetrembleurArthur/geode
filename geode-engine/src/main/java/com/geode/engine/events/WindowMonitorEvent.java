package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMonitorCallbackI;

public class WindowMonitorEvent extends WindowEvent<WindowMonitorI> implements GLFWMonitorCallbackI
{
    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetMonitorCallback(this);
    }

    @Override
    public void invoke(long l, int i)
    {
        getCallbacks().forEach(windowMonitorI -> windowMonitorI.onMonitor(i == GLFW.GLFW_CONNECTED));
    }
}
