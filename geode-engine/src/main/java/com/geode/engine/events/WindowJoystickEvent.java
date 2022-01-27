package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWJoystickCallbackI;

public class WindowJoystickEvent extends WindowEvent<WindowJoystickI> implements GLFWJoystickCallbackI
{
    public WindowJoystickEvent()
    {
        super(0L);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetJoystickCallback(this);
    }

    @Override
    public void invoke(int i, int i1)
    {
        getCallbacks().forEach(windowJoystickI -> windowJoystickI.onJoystick(i1 == GLFW.GLFW_CONNECTED));
    }
}
