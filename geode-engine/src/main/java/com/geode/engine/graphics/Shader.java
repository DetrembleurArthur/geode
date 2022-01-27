package com.geode.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Shader
{
    public static final Shader DEFAULT = new Shader(
            "assets/default.vertex.glsl",
            "assets/default.fragment.glsl",
            true
    );

    private int vertexShader = -1;
    private int fragmentShader = -1;
    private int program = -1;

    public Shader(String vertexSrc, String fragmentSrc, boolean areFile)
    {
        if (areFile)
        {
            initFromFile(vertexSrc, fragmentSrc);
        } else
        {
            init(vertexSrc, fragmentSrc);
        }
    }

    private int loadShader(String src, int type)
    {
        int id = glCreateShader(type);
        glShaderSource(id, src);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE)
        {
            int len = glGetShaderi(id, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Shader compilation");
            System.out.println(glGetShaderInfoLog(id, len));
            System.exit(1);
        }
        return id;
    }

    public void init(String vertexSrc, String fragmentSrc)
    {
        vertexShader = loadShader(vertexSrc, GL_VERTEX_SHADER);
        fragmentShader = loadShader(fragmentSrc, GL_FRAGMENT_SHADER);
        program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE)
        {
            int len = glGetProgrami(program, GL_INFO_LOG_LENGTH);
            System.out.println("Error program");
            System.out.println(glGetProgramInfoLog(program, len));
            System.exit(1);
        }
    }

    private String loadFromFile(String filename) throws IOException
    {
        StringBuilder masterBuf = new StringBuilder();
        String buf;
        BufferedReader br = new BufferedReader(new FileReader(filename));
        while ((buf = br.readLine()) != null)
        {
            masterBuf.append(buf).append("\n");
        }
        br.close();
        return masterBuf.toString();
    }

    public void initFromFile(String vertexSrc, String fragmentSrc)
    {
        String vertex;
        String fragment;
        try
        {
            vertex = loadFromFile(vertexSrc);
            fragment = loadFromFile(fragmentSrc);
            init(vertex, fragment);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void start()
    {
        glUseProgram(program);
    }

    public void stop()
    {
        glUseProgram(0);
    }

    public int getProgram()
    {
        return program;
    }

    public void setUniformf1(String name, float value)
    {
        glUniform1f(glGetUniformLocation(program, name), value);
    }

    public void setUniformf4(String name, Vector4f value)
    {
        glUniform4f(glGetUniformLocation(program, name), value.x, value.y, value.z, value.w);
    }

    public void setUniform1i(String name, int value)
    {
        glUniform1i(glGetUniformLocation(program, name), value);
    }

    public void destroy()
    {
        glDetachShader(program, vertexShader);
        glDetachShader(program, fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        glDeleteProgram(program);
    }

    public void uploadMat4f(String name, Matrix4f matrix)
    {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(16); //4 x 4
        matrix.get(buffer);
        glUniformMatrix4fv(glGetUniformLocation(program, name), false, buffer);
        memFree(buffer);
    }

    public void uploadTexture(String name, int slot)
    {
        var variable = glGetUniformLocation(program, name);
        if (variable != 0)
            glUniform1i(variable, slot);
    }

    public void setUniformf2(String name, Vector2f dimension)
    {
        glUniform2f(glGetUniformLocation(program, name), dimension.x, dimension.y);
    }

    public void setUniform2fv(String name, float[] array)
    {
        glUniform2fv(glGetUniformLocation(program, name), array);
    }

    public void setUniform4fv(String name, float[] array)
    {
        glUniform4fv(glGetUniformLocation(program, name), array);
    }

    public void setUniform1fv(String name, float[] array)
    {
        glUniform1fv(glGetUniformLocation(program, name), array);
    }
}
