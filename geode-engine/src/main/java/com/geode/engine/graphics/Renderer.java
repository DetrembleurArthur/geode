package com.geode.engine.graphics;

public class Renderer
{
	protected final Shader shader;
	protected final Camera camera;

	public Renderer(Shader shader, Camera camera)
	{
		this.shader = shader;
		this.camera = camera;
	}

	public void render(GraphicElement gelem)
	{
		shader.start();
		shader.uploadMat4f("uModel", gelem.getTransformMatrix());
		shader.uploadMat4f("uView", camera.updateViewMatrix());
		shader.uploadMat4f("uProjection", camera.updateProjectionMatrix());
		if(gelem instanceof GameObject && gelem.getTexture() != null)
		{
			shader.setUniform1i("isTextured", 1);
			shader.uploadTexture("TEX_SAMPLER", 0);
		}
		else
		{
			shader.setUniform1i("isTextured", 0);
		}
		gelem.draw();
		shader.stop();
	}
}
