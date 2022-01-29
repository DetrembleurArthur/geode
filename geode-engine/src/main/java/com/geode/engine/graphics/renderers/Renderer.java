package com.geode.engine.graphics.renderers;

import com.geode.engine.core.Application;
import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;
import com.geode.engine.graphics.Shader;
import com.geode.engine.utils.Colors;

public class Renderer
{
	protected Shader shader;
	protected final Camera camera;

	public Renderer(Shader shader)
	{
		this.shader = shader;
		this.camera = Application.getApplication().getScene().getCamera();
	}

	public void render(GameObject go)
	{
		shader.start();
		shader.setUniformf4("fColor", go.getColor());
		shader.uploadMat4f("uModel", go.getTransform().getModel());
		shader.uploadMat4f("uView", camera.updateViewMatrix());
		shader.uploadMat4f("uProjection", camera.getProjection());
		shader.uploadTexture("TEX_SAMPLER", 0);
		if(go.getTexture() != null)
		{
			go.getTexture().active();
			go.getTexture().bind();
		}

		go.getMesh().active();
		if(go.getTexture() != null)
			go.getTexture().unbind();
		shader.stop();
	}
}
