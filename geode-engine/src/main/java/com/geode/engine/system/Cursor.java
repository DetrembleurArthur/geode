package com.geode.engine.system;

import lombok.Getter;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

import java.nio.ByteBuffer;

public class Cursor
{
    @Getter
    private final long id;

    public static final int PIXEL_SIZE = 4 * Byte.BYTES;
    public static final int ARROW = GLFW.GLFW_ARROW_CURSOR;
    public static final int IBEAM =GLFW.GLFW_IBEAM_CURSOR;
    public static final int CROSSHAIR = GLFW.GLFW_CROSSHAIR_CURSOR;
    public static final int HAND = GLFW.GLFW_HAND_CURSOR;
    public static final int HRESIZE = GLFW.GLFW_HRESIZE_CURSOR;
    public static final int VRESIZE = GLFW.GLFW_VRESIZE_CURSOR;

    public Cursor(int id)
    {
        this.id = GLFW.glfwCreateStandardCursor(id);
    }

    public Cursor(int width, int height, int[] pixels)
    {
        ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);
        for(int px : pixels)
        {
            buffer.putInt(px);
        }
        buffer.flip();
        GLFWImage image = GLFWImage.malloc();
        image.set(width, height, buffer);
        id = GLFW.glfwCreateCursor(image, 0, 0);
        buffer.clear();
    }

    public void destroy()
    {
        GLFW.glfwDestroyCursor(id);
    }
}
