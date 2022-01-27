package com.geode.engine.events;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWDropCallbackI;
import org.lwjgl.system.Pointer;

import static org.lwjgl.system.MemoryUtil.memGetAddress;
import static org.lwjgl.system.MemoryUtil.memUTF8;

public class WindowDropEvent extends WindowEvent<WindowDropI> implements GLFWDropCallbackI
{
    public WindowDropEvent(Long id)
    {
        super(id);
    }

    @Override
    protected void register(long windowId)
    {
        GLFW.glfwSetDropCallback(windowId, this);
    }

    @Override
    public void invoke(long l, int count, long paths)
    {
        String[] items = new String[count];
        for(int i = 0; i < count; i++)
        {
            items[i] = memUTF8(memGetAddress(paths + Pointer.POINTER_SIZE * i));
        }
        getCallbacks().forEach(windowDropI -> windowDropI.onDrop(items));
    }
}
