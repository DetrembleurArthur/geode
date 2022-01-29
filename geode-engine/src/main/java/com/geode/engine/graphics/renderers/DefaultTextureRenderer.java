package com.geode.engine.graphics.renderers;

import com.geode.engine.graphics.Camera;
import com.geode.engine.graphics.Shader;

public class DefaultTextureRenderer extends Renderer
{
    public DefaultTextureRenderer()
    {
        super(Shader.DEFAULT_TEXTURED);
    }
}
