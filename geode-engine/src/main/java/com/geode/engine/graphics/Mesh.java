package com.geode.engine.graphics;

import lombok.Getter;
import lombok.Setter;

import static org.lwjgl.opengl.GL11.*;

public class Mesh
{
	@Getter protected VertexArray vertexArray;
	@Getter protected VertexBuffer vertexBuffer;
	@Getter protected IndexBuffer indexBuffer;
	@Getter @Setter
	protected int primitive;

	public Mesh(float[] vertices, int[] indexes)
	{
		setTriangleRenderMode();
		initVertices(vertices, indexes);
	}

	protected void initVertices(float[] vertices, int[] indexes)
	{
		vertexArray = new VertexArray();
		vertexBuffer = new VertexBuffer();
		vertexBuffer.setStaticVertices(vertices);
		indexBuffer = new IndexBuffer();
		indexBuffer.setStaticElements(indexes);
	}

	public void destroy()
	{
		vertexArray.disableAttribArray(2);
		vertexBuffer.unbind();
		vertexBuffer.destroy();
		indexBuffer.destroy();
		vertexArray.unbind();
		vertexArray.destroy();
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
}
