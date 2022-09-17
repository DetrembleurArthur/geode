package com.geode.engine.core;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

public class WindowHints
{
    private final ArrayList<Runnable> setupHintsCallbacks = new ArrayList<>();

    public static WindowHints create()
    {
        return new WindowHints();
    }

    public WindowHints hint(int type, int value)
    {
        setupHintsCallbacks.add(() -> glfwWindowHint(type, value));
        return this;
    }

    public void apply()
    {
        for(Runnable runnable : setupHintsCallbacks)
            runnable.run();
        String osName = System.getProperty("os.name");
        System.out.println("OS: " + osName);
        if (osName.toUpperCase().contains("MAC"))
        {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        }
        setupHintsCallbacks.clear();
    }

    public WindowHints resizeable(boolean value)
    {
        return hint(GLFW_RESIZABLE, value ? GLFW_TRUE : GLFW_FALSE);
    }

    public WindowHints visible(boolean value)
    {
        return hint(GLFW_VISIBLE, value ? GLFW_TRUE : GLFW_FALSE);
    }

    public WindowHints decorated(boolean value)
    {
        return hint(GLFW_DECORATED, value ? GLFW_TRUE : GLFW_FALSE);
    }

    public WindowHints focus(boolean value)
    {
        return hint(GLFW_FOCUSED, value ? GLFW_TRUE : GLFW_FALSE);
    }

    public WindowHints autoIconify(boolean value)
    {
        return hint(GLFW_AUTO_ICONIFY, value ? GLFW_TRUE : GLFW_FALSE);
    }

    public WindowHints floating(boolean value)
    {
        return hint(GLFW_FLOATING, value ? GLFW_TRUE : GLFW_FALSE);
    }

    public WindowHints maximized(boolean value)
    {
        return hint(GLFW_MAXIMIZED, value ? GLFW_TRUE : GLFW_FALSE);
    }

    public WindowHints centerCursor(boolean value)
    {
        return hint(GLFW_CENTER_CURSOR, value ? GLFW_TRUE : GLFW_FALSE);
    }

    public WindowHints focusOnShow(boolean value)
    {
        return hint(GLFW_FOCUS_ON_SHOW, value ? GLFW_TRUE : GLFW_FALSE);
    }

    public WindowHints scaleToMonitor(boolean value)
    {
        return hint(GLFW_SCALE_TO_MONITOR, value ? GLFW_TRUE : GLFW_FALSE);
    }

    public WindowHints transparent(boolean value)
    {
        return hint(GLFW_TRANSPARENT_FRAMEBUFFER, value ? GLFW_TRUE : GLFW_FALSE);
    }
}
