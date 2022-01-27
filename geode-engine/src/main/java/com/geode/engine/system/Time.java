package com.geode.engine.system;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.glfw.GLFW;

public class Time
{
    @Getter @Setter
    private static float dt;

    public static float getTime()
    {
        return (float) GLFW.glfwGetTime() * 1000f;
    }

    public static void setBaseTime(float sec)
    {
        GLFW.glfwSetTime(sec);
    }

    public static long getTimerValue()
    {
        return GLFW.glfwGetTimerValue();
    }

    public static long getTimerFrequency()
    {
        return GLFW.glfwGetTimerFrequency();
    }
}
