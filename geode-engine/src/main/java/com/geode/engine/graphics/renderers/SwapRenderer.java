package com.geode.engine.graphics.renderers;

import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;
import com.geode.engine.graphics.Shader;

public class SwapRenderer extends Renderer
{
    public SwapRenderer()
    {
        super(null);
    }

    @Override
    public void render(GameObject go)
    {
        shader = go.isTextured() ? Shader.DEFAULT_TEXTURED : Shader.DEFAULT;
        super.render(go);
    }
}
