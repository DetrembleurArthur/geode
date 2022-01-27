package com.geode.engine.graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL15C;

import java.nio.FloatBuffer;

public class VertexBuffer
{
    private int id;

    public VertexBuffer()
    {
        id = GL15.glGenBuffers();
    }

    public void bind()
    {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
    }

    public void unbind()
    {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void setStaticVertices(float[] vertices)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices).flip();
        bind();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        unbind();
    }

    public void destroy()
    {
        GL15C.glDeleteBuffers(id);
    }
}
