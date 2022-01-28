package com.geode.engine.system;

import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwSetGamma;

@Getter
public class Monitor
{
    private static final ArrayList<Monitor> monitors = new ArrayList<>();

    private Long id;

    private static Monitor SELECTED = getPrimary();

    public static Monitor getSelected()
    {
        return SELECTED;
    }

    public static void setSelected(Monitor monitor)
    {
        SELECTED = monitor;
    }

    public static Monitor getPrimary()
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

    public Vector2i getPhysicalSize()
    {
        int[] w = new int[1];
        int[] h = new int[1];
        GLFW.glfwGetMonitorPhysicalSize(id, w, h);
        return new Vector2i(w[0], h[0]);
    }

    public Vector2f getContentScale()
    {
        float[] sx = new float[1];
        float[] sy = new float[1];
        GLFW.glfwGetMonitorContentScale(id, sx, sy);
        return new Vector2f(sx[0], sy[0]);
    }

    public Vector2i getPosition()
    {
        int[] x = new int[1];
        int[] y = new int[1];
        GLFW.glfwGetMonitorPos(id, x, y);
        return new Vector2i(x[0], y[0]);
    }

    public Vector4i getWorkArea()
    {
        int[] x = new int[1];
        int[] y = new int[1];
        int[] w = new int[1];
        int[] h = new int[1];
        GLFW.glfwGetMonitorWorkarea(id, x, y, w, h);
        return new Vector4i(x[0], y[0], w[0], h[0]);
    }

    public void setGamma(float gamma)
    {
        glfwSetGamma(id, gamma);
    }

    public static void setAllGamma(float gamma)
    {
        for(Monitor monitor : getMonitors())
        {
            monitor.setGamma(gamma);
        }
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
