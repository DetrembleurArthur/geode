package com.geode.engine.graphics.prefabs;

import com.geode.engine.graphics.Mesh;
import com.geode.engine.graphics.MeshStructure;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL15;

public class MeshFactory
{
    public static Mesh rect(boolean dynamic, boolean textured)
    {
        float[] positions = new float[]{
                0f, 0f,
                0f, 1f,
                1f, 1f,
                1f, 0f,
        };

        float[] uvs = new float[]{
                0, 0,
                0, 1,
                1, 1,
                1, 0,
        };

        int[] indices = new int[]{
                0, 1, 2,
                0, 2, 3
        };

        MeshStructure context = new MeshStructure();
        MeshStructure.Attribute positionsAttr = MeshStructure.Attribute.builder().data(positions).size(2).build();
        context.addAttribute(positionsAttr);
        if(textured)
        {
            MeshStructure.Attribute uvsAttr = MeshStructure.Attribute.builder().data(uvs).size(2).build();
            context.addAttribute(uvsAttr);
        }
        return new Mesh(context, indices, dynamic ? GL15.GL_DYNAMIC_DRAW : GL15.GL_STATIC_DRAW);
    }

    public static Mesh circle(boolean dynamic, boolean textured, int nPoints)
    {
        Vector2f glCenter = new Vector2f(0.5f, 0.5f);
        float angle = (float)Math.toRadians(360f / nPoints);
        float[] vertex = new float[(nPoints + 1) * 4];
        vertex[0] = glCenter.x;
        vertex[1] = glCenter.y;
        int j = 2;
        for(int i = 0; i < nPoints; i++)
        {
            vertex[j++] = (float) (glCenter.x + (0.5 * Math.sin(angle * i)));
            vertex[j++] = (float) (glCenter.y + (-0.5 * Math.cos(angle * i)));
        }
        int[] indexes = new int[nPoints * 3];
        j = 0;
        for(int i = 0; i < nPoints - 1; i++)
        {
            indexes[j++] = 0;
            indexes[j++] = i + 1;
            indexes[j++] = i + 2;
        }
        indexes[j++] = 0;
        indexes[j++] = nPoints;
        indexes[j] = 1;

        MeshStructure context = new MeshStructure();
        MeshStructure.Attribute positionsAttr = MeshStructure.Attribute.builder().data(vertex).size(2).build();
        context.addAttribute(positionsAttr);
        if(textured)
        {
            MeshStructure.Attribute uvsAttr = MeshStructure.Attribute.builder().data(vertex).size(2).build();
            context.addAttribute(uvsAttr);
        }
        return new Mesh(context, indexes, dynamic ? GL15.GL_DYNAMIC_DRAW : GL15.GL_STATIC_DRAW);
    }

    public static Mesh generic(float[] positions, float[] uvs, int[] indexes, boolean textured)
    {
        MeshStructure context = new MeshStructure();
        MeshStructure.Attribute positionsAttr = MeshStructure.Attribute.builder().data(positions).size(2).build();
        context.addAttribute(positionsAttr);
        if(textured)
        {
            MeshStructure.Attribute uvsAttr = MeshStructure.Attribute.builder().data(uvs).size(2).build();
            context.addAttribute(uvsAttr);
        }
        return new Mesh(context, indexes, GL15.GL_DYNAMIC_DRAW);
    }

    public static Mesh line(Vector2f ... points)
    {
        int[] indexes = new int[points.length];
        float[] vertex = new float[points.length * 2];
        for(int i = 0; i < points.length; i++)
        {
            indexes[i] = i;
            vertex[i * 2] = points[i].x;
            vertex[i * 2 + 1] = points[i].y;
        }
        MeshStructure context = new MeshStructure();
        MeshStructure.Attribute positionAttr = MeshStructure.Attribute.builder().data(vertex).size(2).build();
        context.addAttribute(positionAttr);

        return new Mesh(context, indexes, GL15.GL_DYNAMIC_DRAW);
    }
}
