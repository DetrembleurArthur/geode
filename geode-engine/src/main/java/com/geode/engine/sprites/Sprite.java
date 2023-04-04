package com.geode.engine.sprites;

import com.geode.engine.graphics.Texture;
import org.joml.Vector2f;

public class Sprite
{
    private Texture texture;
    private Vector2f[] textCoords;

    public Sprite(Texture texture)
    {
        this(texture, new Vector2f[]
                {
                        new Vector2f(0, 0), //top left
                        new Vector2f(0, 1), //bottom left
                        new Vector2f(1, 1), //bottom right
                        new Vector2f(1, 0) //top right
                });
    }

    public Sprite(Texture texture, Vector2f[] textCoords)
    {
        this.texture = texture;
        this.textCoords = textCoords;
    }

    public Texture getTexture()
    {
        return texture;
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    public Vector2f[] getTextCoords()
    {
        return textCoords;
    }

    public void setTextCoords(Vector2f[] textCoords)
    {
        this.textCoords = textCoords;
    }
}
