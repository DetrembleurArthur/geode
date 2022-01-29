package com.geode.engine.graphics;

import com.geode.engine.entity.GameObject;
import com.geode.engine.utils.Colors;

public class Renderer
{
	protected final Shader shader;
	protected final Camera camera;

	public Renderer(Shader shader, Camera camera)
	{
		this.shader = shader;
		this.camera = camera;
	}

	public void render(GameObject go)
	{
		shader.start();
		shader.uploadMat4f("uModel", go.getTransform().getModel());
		shader.uploadMat4f("uView", camera.updateViewMatrix());
		shader.uploadMat4f("uProjection", camera.getProjection());
		shader.uploadTexture("TEX_SAMPLER", 0);
		go.getTexture().active();
		go.getTexture().bind();
		go.getMesh().active();
		go.getTexture().unbind();
		shader.stop();
	}
}
