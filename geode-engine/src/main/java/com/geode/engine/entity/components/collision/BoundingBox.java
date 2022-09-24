package com.geode.engine.entity.components.collision;

import com.geode.engine.entity.Rect;
import com.geode.engine.utils.MathUtils;
import org.joml.Vector2f;

public class BoundingBox
{
	private float x;
	private float y;
	private float width;
	private float height;

	public BoundingBox(float x, float y, float width, float height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public float getWidth()
	{
		return width;
	}

	public float getHeight()
	{
		return height;
	}

	public void setX(float x)
	{
		this.x = x;
	}

	public void setY(float y)
	{
		this.y = y;
	}

	public void setWidth(float width)
	{
		this.width = width;
	}

	public void setHeight(float height)
	{
		this.height = height;
	}

	public boolean isCollision(final BoundingBox other)
	{
		return x < other.x + other.width && x + width > other.x && y < other.y + other.height && y + height > other.y;
	}

	public boolean isCollision(final Vector2f pos)
	{
		return MathUtils.boxContains(new Vector2f(x, y), new Vector2f(width, height), pos);
	}

	public Rect asRectangle()
	{
		var r = new Rect();
		r.getTransform().setTopLeftPosition(new Vector2f(x, y));
		r.getTransform().setSize2D(width, height);
		return r;
	}

	@Override
	public Object clone()
	{
		return new BoundingBox(x, y, width, height);
	}

	@Override
	public String toString()
	{
		return "BoundingBox{" +
				"x=" + x +
				", y=" + y +
				", width=" + width +
				", height=" + height +
				'}';
	}
}
