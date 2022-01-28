package com.geode.engine.graphics;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL14;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture
{
    private String path;
    private final int id;
    private Vector2f dimension;

    public Texture(String path)
    {
        this.path = path;
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        init(path);
        glBindTexture(GL_TEXTURE_2D, 0);
    }


    public void init(String path)
    {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(path, width, height, channels, 0);
        if (image != null)
        {
            glTexImage2D(GL_TEXTURE_2D, 0, channels.get(0) == 4 ? GL_RGBA : GL_RGB, width.get(0), height.get(0), 0, channels.get(0) == 4 ? GL_RGBA : GL_RGB, GL_UNSIGNED_BYTE, image);
            initParameters();
            dimension = new Vector2f(width.get(0), height.get(0));
            stbi_image_free(image);
        } else
        {
            System.err.println("image " + path + " can not be loaded");
        }
    }

    private void initParameters()
    {
        enableRepeat();
        enableNearest();
    }

    private void enableWrapParameter(int param)
    {
        bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, param);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, param);
        unbind();
    }

    private void enableFilterParameter(int param)
    {
        bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, param);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, param);
        unbind();
    }

    public void enableLinear()
    {
        enableFilterParameter(GL_LINEAR);
    }

    public void enableNearest()
    {
        enableFilterParameter(GL_NEAREST);
    }

    public void enableRepeat()
    {
        enableWrapParameter(GL_REPEAT);
    }

    public void enableMirroredRepeat()
    {
        enableWrapParameter(GL14.GL_MIRRORED_REPEAT);
    }

    public void enableClampToEdge()
    {
        enableWrapParameter(GL_CLAMP_TO_EDGE);
    }

    public void enableClampToBorder()
    {
        enableWrapParameter(GL_CLAMP_TO_BORDER);
    }

    public void active()
    {
        glActiveTexture(GL_TEXTURE0);
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void destroy()
    {
        glDeleteTextures(id);
    }

    public Vector2f getDimension()
    {
        return new Vector2f(dimension);
    }

    public float getWidth()
    {
        return dimension.x;
    }

    public float getHeight()
    {
        return dimension.y;
    }

    public String getPath()
    {
        return path;
    }

    public Vector2f[] getUV2D(float x, float y, float w, float h)
    {
        return new Vector2f[]
                {
                        new Vector2f(x / getWidth(), y / getHeight()), //TOP LEFT
                        new Vector2f(x / getWidth(), y / getHeight() + h / getHeight()), //BOTTOM LEFT,
                        new Vector2f(x / getWidth() + w / getWidth(), y / getHeight() + h / getHeight()), //BOTTOM RIGHT
                        new Vector2f(x / getWidth() + w / getWidth(), y / getHeight()), //TOP RIGHT
                };
    }

    public int getId()
    {
        return id;
    }
}

