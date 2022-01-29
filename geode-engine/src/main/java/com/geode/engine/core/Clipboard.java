package com.geode.engine.core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

public class Clipboard
{
    public static void set(String utf8s)
    {
        GLFW.glfwSetClipboardString(MemoryUtil.NULL, utf8s);
    }

    public static String get()
    {
        return GLFW.glfwGetClipboardString(MemoryUtil.NULL);
    }
}
