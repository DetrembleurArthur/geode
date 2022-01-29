package com.geode.engine.core;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.glfw.GLFW;

public class Time
{
    @Getter @Setter
    private static float dt;

    @Getter
    private static final float startTime = Time.getTime();

    public static float getTime()
    {
        return (float) GLFW.glfwGetTime();
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

    public static float getElapsedTime()
    {
        return Time.getTime() - startTime;
    }
}
