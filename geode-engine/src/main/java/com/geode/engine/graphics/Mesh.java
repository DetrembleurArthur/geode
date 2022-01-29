package com.geode.engine.graphics;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL15C;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Mesh
{
    @Getter
    @Setter
    private int primitive;
    private int vao = 0;
    private int vbo = 0;
    private int ebo = 0;
    private int elementLen = 0;
    private final MeshContext context;

    public Mesh(MeshContext context, int[] indices)
    {
        this.context = context;
        setTriangleRenderMode();
        init(indices);
    }

    private void init(int[] indices)
    {
        elementLen = indices.length;
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        vbo = GL15.glGenBuffers();
        ebo = GL15.glGenBuffers();

        FloatBuffer vertexBuffer = context.buildVertices();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(indices.length);
        elementBuffer.put(indices).flip();

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL15.GL_STATIC_DRAW);

        context.setupAttribPointers();
        context.enableAttribs();
    }

    public void destroy()
    {
        GL30.glBindVertexArray(vao);
        context.disableAttribs();
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vao);
        GL15C.glDeleteBuffers(vbo);
        GL15C.glDeleteBuffers(ebo);
    }

    public void setPointsRenderMode()
    {
        setPrimitive(GL_POINTS);
    }

    public void setTriangleRenderMode()
    {
        setPrimitive(GL_TRIANGLES);
    }

    public void setTriangleStripRenderMode()
    {
        setPrimitive(GL_TRIANGLE_STRIP);
    }

    public void setTriangleFanRenderMode()
    {
        setPrimitive(GL_TRIANGLE_FAN);
    }

    public void setLineLoopRenderMode()
    {
        setPrimitive(GL_LINE_LOOP);
    }

    public void setLinesRenderMode()
    {
        setPrimitive(GL_LINES);
    }

    public void setLineStripRenderMode()
    {
        setPrimitive(GL_LINE_STRIP);
    }

    public void active()
    {
        GL30.glBindVertexArray(vao);
        context.enableAttribs();

        glDrawElements(primitive, elementLen, GL_UNSIGNED_INT, 0);

        context.disableAttribs();
        GL30.glBindVertexArray(0);
    }
}
