package com.geode.engine.utils;

import com.geode.engine.tweening.TweenFunction;
import org.joml.Vector4f;

import java.awt.*;
import java.util.Random;

public class Colors
{
    public static final Vector4f RED = new Vector4f(1, 0, 0, 1);
    public static final Vector4f GREEN = new Vector4f(0, 1, 0, 1);
    public static final Vector4f BLUE = new Vector4f(0, 0, 1, 1);
    public static final Vector4f TRANSPARENT = new Vector4f(0, 0, 0, 0);
    public static final Vector4f WHITE = new Vector4f(1, 1, 1, 1);
    public static final Vector4f BLACK = new Vector4f(0, 0, 0, 1);
    public static final Vector4f YELLOW = new Vector4f(1, 1, 0, 1);
    public static final Vector4f MAGENTA = new Vector4f(1, 0, 1, 1);
    public static final Vector4f ORANGE = new Vector4f(1, 0.5f, 0, 1);
    public static final Vector4f PINK = new Vector4f(1, 0, 0.5f, 1);
    public static final Vector4f CYAN = new Vector4f(0, 1, 0.8f, 1);
    public static final Vector4f LIME = new Vector4f(0.6f, 1, 0, 1);
    public static final Vector4f PURPLE = new Vector4f(0.8f, 0, 1, 1);

    public static Vector4f random()
    {
        Random rand = new Random();
        return new Vector4f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
    }

    public static Vector4f rgb(float r, float g, float b)
    {
        return new Vector4f(r / 255f, g / 255f, b / 255f, 1f);
    }

    public static Vector4f rgba(float r, float g, float b, float a)
    {
        Vector4f color = rgb(r, g, b);
        color.w = a / 255f;
        return color;
    }

    public static Vector4f awtColor(Color awt)
    {
        return new Vector4f(awt.getRed() / 255f,awt.getGreen() / 255f,awt.getBlue() / 255f,awt.getAlpha() / 255f);
    }

    public static Vector4f interpolate(Vector4f c1, Vector4f c2, float perc, TweenFunction function)
    {
        perc = function.f(perc);
        return new Vector4f(c1).mul(1f-perc).add(new Vector4f(c2).mul(perc));
    }

}
