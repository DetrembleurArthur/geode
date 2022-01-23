package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallbackI;

public class WindowTextInputEvent extends WindowEvent<WindowTextInputI> implements GLFWCharCallbackI
{
    public WindowTextInputEvent(long windowId)
    {
        super(windowId);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetCharCallback(windowId, this);
    }

    @Override
    public void invoke(long l, int i)
    {
        getCallbacks().forEach(windowTextInputI -> windowTextInputI.onTextInput((char)i));
    }
}
