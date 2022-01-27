package com.geode.engine.graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;

public class IndexBuffer
{
    private int id;
    private int len;

    public IndexBuffer()
    {
        id = GL15.glGenBuffers();
    }

    public void bind()
    {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
    }

    public void unbind()
    {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void setStaticElements(int[] indices)
    {
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices).flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        len = indices.length;
    }

    public void drawElements(int drawType)
    {
        bind();
        glDrawElements(drawType, len, GL_UNSIGNED_INT, 0);
        unbind();
    }
}
