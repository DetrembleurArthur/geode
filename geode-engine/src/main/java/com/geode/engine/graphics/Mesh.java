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

@Getter
public class Mesh
{

    @Setter
    private int primitive;
    private int vao = 0;
    private int vbo = 0;
    private int ebo = 0;
    private int elementLen = 0;
    @Setter
    private int lineWeight = 0;
    private final MeshStructure context;

    public Mesh(MeshStructure context, int[] indices, int dataDrawPolicy)
    {
        this.context = context;
        setTriangleRenderMode();
        init(indices, dataDrawPolicy);
    }

    public void updateVertex(int dataDrawPolicy)
    {
        FloatBuffer vertexBuffer = context.buildVertices();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, dataDrawPolicy);
    }

    private void init(int[] indices, int dataDrawPolicy)
    {
        elementLen = indices.length;
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        vbo = GL15.glGenBuffers();
        ebo = GL15.glGenBuffers();

        updateVertex(dataDrawPolicy);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(indices.length);
        elementBuffer.put(indices).flip();

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, dataDrawPolicy);

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
        if(lineWeight > 0)
            glLineWidth(lineWeight);
        GL30.glBindVertexArray(vao);
        context.enableAttribs();

        glDrawElements(primitive, elementLen, GL_UNSIGNED_INT, 0);

        context.disableAttribs();
        GL30.glBindVertexArray(0);
        if(lineWeight > 0)
            glLineWidth(0);
    }

    public int getVertexNumber()
    {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        int size = GL15.glGetBufferParameteri(GL15.GL_ARRAY_BUFFER, GL15.GL_BUFFER_SIZE);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return size / context.getVertexSize();
    }

    public int getIndicesNumber()
    {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        int size = GL15.glGetBufferParameteri(GL15.GL_ELEMENT_ARRAY_BUFFER, GL15.GL_BUFFER_SIZE);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        return size / Integer.BYTES;
    }

    public void setVertex(FloatBuffer buffer, int i)
    {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, (long) i * context.getVertexSize(), buffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void show()
    {
        System.out.print("Attribute sizes:\t");
        for(MeshStructure.Attribute attribute : context.getAttributes())
            System.out.print(attribute.size + " ");
        System.out.println();

        int n = getVertexNumber();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(n * (context.getVertexSize() / Float.BYTES));
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glGetBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        System.out.println("Vertices: " + n);
        for(int i = 0; i < n; i++)
        {
            for(MeshStructure.Attribute attribute : context.getAttributes())
            {
                for(int j = 0; j < attribute.size; j++)
                {
                    System.out.print(buffer.get() + " ");
                }
                System.out.print("\t");
            }
            System.out.println();
        }
    }
}
