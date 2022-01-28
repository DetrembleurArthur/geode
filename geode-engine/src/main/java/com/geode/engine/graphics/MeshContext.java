package com.geode.engine.graphics;

import lombok.Builder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

public class MeshContext
{
    @Builder
    public static class Attribute
    {
        public int size;
        public float[] data;
    }

    private ArrayList<Attribute> attributes;

    public MeshContext()
    {
        attributes = new ArrayList<>();
    }

    public MeshContext addAttribute(Attribute attribute)
    {
        attributes.add(attribute);
        return this;
    }

    public int getVertexSize()
    {
        return attributes.stream().mapToInt(attribute -> attribute.size).sum() * Float.BYTES;
    }

    public void setupAttribPointers()
    {
        int size = getVertexSize();
        long ptr = 0;
        for(int i = 0; i < attributes.size(); i++)
        {
            GL20.glVertexAttribPointer(i, attributes.get(i).size, GL_FLOAT, false, size, ptr * Float.BYTES);
            ptr += attributes.get(i).size;
        }
    }

    public void enableAttribs()
    {
        for(int i = 0; i < attributes.size(); i++)
        {
            GL20.glEnableVertexAttribArray(i);
        }
    }

    public void disableAttribs()
    {
        for(int i = 0; i < attributes.size(); i++)
        {
            GL20.glDisableVertexAttribArray(i);
        }
    }

    public FloatBuffer buildVertices()
    {
        //nombre de vertexs
        int size = attributes.get(0).data.length / attributes.get(0).size;
        //nombre de float total
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(size * (getVertexSize() / Float.BYTES));
        for(int i = 0; i < size; i++)
        {
            for(Attribute attribute : attributes)
            {
                verticesBuffer.put(attribute.data, i * attribute.size, attribute.size);
            }
        }
        return verticesBuffer.flip();
    }
}
