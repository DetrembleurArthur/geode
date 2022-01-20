package com.geode.crypto.pipeline;

import java.io.Serializable;
import java.util.ArrayList;

public class Pipeline implements Layer
{
    private final ArrayList<Layer> layers;

    public Pipeline()
    {
        layers = new ArrayList<>();
    }

    @Override
    public Serializable in(Serializable obj)
    {
        for(Layer layer : layers)
            obj = layer.in(obj);
        return obj;
    }

    @Override
    public Serializable out(Serializable obj)
    {
        for(Layer layer : layers)
            obj = layer.out(obj);
        return obj;
    }
}
