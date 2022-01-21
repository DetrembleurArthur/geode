package com.geode.engine.system;

import lombok.Getter;
import org.lwjgl.glfw.GLFW;

@Getter
public class Monitor
{
    private Long id;

    public static Monitor getDefault()
    {
        return new Monitor(GLFW.glfwGetPrimaryMonitor());
    }

    public Monitor(long id)
    {
        this.id = id;
    }
}
