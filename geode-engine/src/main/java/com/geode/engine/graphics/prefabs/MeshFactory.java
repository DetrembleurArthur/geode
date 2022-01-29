package com.geode.engine.graphics.prefabs;

import com.geode.engine.graphics.Mesh;
import com.geode.engine.graphics.MeshContext;
import org.lwjgl.opengl.GL15;

public class MeshFactory
{
    public static Mesh rect(boolean dynamic, boolean textured)
    {
        float[] positions = new float[]{
                0f, 0f, 0f,
                0f, 1f, 0f,
                1f, 1f, 0f,
                1f, 0f, 0f,
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

        MeshContext context = new MeshContext();
        MeshContext.Attribute positionsAttr = MeshContext.Attribute.builder().data(positions).size(3).build();
        context.addAttribute(positionsAttr);
        if(textured)
        {
            MeshContext.Attribute uvsAttr = MeshContext.Attribute.builder().data(uvs).size(2).build();
            context.addAttribute(uvsAttr);
        }
        return new Mesh(context, indices, dynamic ? GL15.GL_DYNAMIC_DRAW : GL15.GL_STATIC_DRAW);
    }
}
