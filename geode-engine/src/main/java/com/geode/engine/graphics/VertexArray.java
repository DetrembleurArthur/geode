package com.geode.engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VertexArray
{
    public static final int POSITION_SIZE = 3;
    public static final int COLOR_SIZE = 4;
    public static final int UV_SIZE = 2;
    public static final int FLOAT_SIZE = Float.BYTES;
    public static final int VERTEX_SIZE = (POSITION_SIZE + UV_SIZE) * FLOAT_SIZE;

    private int id;

    public VertexArray()
    {
        id = GL30.glGenVertexArrays();
        setAttribute(0, POSITION_SIZE, GL11.GL_FLOAT, 0);
        setAttribute(1, UV_SIZE, GL11.GL_FLOAT, (POSITION_SIZE) * FLOAT_SIZE);
    }

    public void bind()
    {
        GL30.glBindVertexArray(id);
    }

    public void unbind()
    {
        GL30.glBindVertexArray(0);
    }

    public void setAttribute(int index, int size, int type, int pointer)
    {
        GL20.glVertexAttribPointer(index, size, type, false, VERTEX_SIZE, pointer);
        GL20.glEnableVertexAttribArray(index);
    }

    public void enableAttribArray(int count)
    {
        while(count-- >= 1)
            GL20.glEnableVertexAttribArray(count-1);
    }

    public void disableAttribArray(int count)
    {
        while(count-- >= 1)
            GL20.glDisableVertexAttribArray(count-1);
    }

    public void destroy()
    {
        GL30.glDeleteVertexArrays(id);
        id = -1;
    }
}
