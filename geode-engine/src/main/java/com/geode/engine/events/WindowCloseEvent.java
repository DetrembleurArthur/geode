package com.geode.engine.events;

import org.lwjgl.glfw.GLFWWindowCloseCallbackI;

public class WindowCloseEvent extends WindowEvent<WindowCloseI> implements GLFWWindowCloseCallbackI
{
    @Override
    public void invoke(long l)
    {
        getCallbacks().forEach(WindowCloseI::onWindowClose);
    }
}
