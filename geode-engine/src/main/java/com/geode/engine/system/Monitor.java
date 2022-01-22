package com.geode.engine.system;

import lombok.Getter;
import org.joml.Vector2i;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.ArrayList;
import java.util.Objects;

@Getter
public class Monitor
{
    private static final ArrayList<Monitor> monitors = new ArrayList<>();

    private Long id;

    public static Monitor getDefault()
    {
        return new Monitor(GLFW.glfwGetPrimaryMonitor());
    }

    public Monitor(long id)
    {
        this.id = id;
    }

    public GLFWVidMode getVideoMode()
    {
        return GLFW.glfwGetVideoMode(id);
    }

    public Vector2i getSize()
    {
        GLFWVidMode vidMode = getVideoMode();

        return new Vector2i(vidMode.width(), vidMode.height());
    }

    public String getName()
    {
        return GLFW.glfwGetMonitorName(id);
    }

    public static ArrayList<Monitor> getMonitors()
    {
        if(monitors.isEmpty())
        {
            PointerBuffer buffer = GLFW.glfwGetMonitors();
            for(int i = 0; i < Objects.requireNonNull(buffer).capacity(); i++)
            {
                monitors.add(new Monitor(buffer.get(i)));
            }
            buffer.clear();
        }
        return monitors;
    }
}
