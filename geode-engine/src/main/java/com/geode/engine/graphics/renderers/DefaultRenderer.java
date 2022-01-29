package com.geode.engine.graphics.renderers;

import com.geode.engine.graphics.Camera;
import com.geode.engine.graphics.Shader;

public class DefaultRenderer extends Renderer
{
    public DefaultRenderer()
    {
        super(Shader.DEFAULT);
    }
}
