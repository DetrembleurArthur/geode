package com.geode.engine;

import com.geode.engine.dispatchers.ResourceRef;
import com.geode.engine.graphics.Texture;
import com.geode.engine.graphics.ui.text.Font;

public class MainResourceHolder
{
    @ResourceRef("blob.png")
    public Texture blob;

    @ResourceRef("bg.png")
    public Texture bg;

    @ResourceRef("artorias.png")
    public Texture artorias;

    @ResourceRef("icon.png")
    public Texture icon;

    /*@ResourceRef("t.fnt")
    public Font font;*/
}
