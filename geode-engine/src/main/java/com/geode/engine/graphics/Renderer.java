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
		shader.setUniformf4("fColor", Colors.RED);
		if(go.getTexture() != null)
		{
			shader.setUniform1i("isTextured", 1);
			shader.uploadTexture("TEX_SAMPLER", 0);
		}
		else
		{
			shader.setUniform1i("isTextured", 0);
		}
		Mesh mesh = go.getMesh();
		mesh.getVertexArray().bind();
		mesh.getVertexArray().enableAttribArray(2);
		go.getTexture().active();
		go.getTexture().bind();
		mesh.getIndexBuffer().drawElements(mesh.getPrimitive());
		mesh.getVertexArray().disableAttribArray(2);
		mesh.getVertexArray().unbind();
		go.getTexture().unbind();
		shader.stop();
	}
}
