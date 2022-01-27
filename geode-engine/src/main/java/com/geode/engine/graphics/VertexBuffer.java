package com.geode.engine.graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

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

    public void setStaticVertices(float[] vertices)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices).flip();
        bind();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }
}
