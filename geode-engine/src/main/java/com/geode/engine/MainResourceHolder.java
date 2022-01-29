package com.geode.engine;

import com.geode.engine.dispatchers.ResourceRef;
import com.geode.engine.graphics.Texture;

public class MainResourceHolder
{
    @ResourceRef("blob.png")
    public Texture blob;

    @ResourceRef("bg.png")
    public Texture bg;
}
