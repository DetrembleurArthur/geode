package com.geode.engine.entity;

import com.geode.engine.graphics.prefabs.MeshFactory;
import com.geode.engine.utils.Colors;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL15;

import java.util.ArrayList;
import java.util.List;

public class Line extends SingleObject
{
    @Getter
    private final ArrayList<Vector2f> pointsCache = new ArrayList<>();
    @Getter @Setter
    private int limit = Integer.MAX_VALUE;

    public Line()
    {
        super(null);
        setMesh(MeshFactory.line());
        setColor(Colors.BLACK);
    }

    public void setPoint(int i, Vector2f point)
    {
        var points = getMesh().getContext().getAttributes().get(0).data;
        points[i * 2] = point.x;
        points[i * 2 + 1] = point.y;
        getMesh().updateVertex(GL15.GL_DYNAMIC_DRAW);
    }

    public Vector2f getPoint(int i)
    {
        return new Vector2f(pointsCache.get(i));
    }

    public void addPoint(Vector2f point)
    {
        if(pointsCache.size() >= limit)
            pointsCache.remove(0);
        pointsCache.add(point);
        Vector2f[] points = new Vector2f[pointsCache.size()];
        pointsCache.toArray(points);
        setMesh(MeshFactory.line(points));
        getMesh().setLineStripRenderMode();
    }

    public void setup(int n)
    {
        for(int i = 0; i < n; i++)
            addPoint(new Vector2f());
    }

    public void setWeight(int weight)
    {
        getMesh().setLineWeight(weight);
    }
}
