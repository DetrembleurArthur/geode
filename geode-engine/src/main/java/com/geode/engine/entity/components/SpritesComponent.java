package com.geode.engine.entity.components;

import com.geode.engine.entity.GameObject;
import com.geode.engine.sprites.SpriteSheet;
import com.geode.engine.timer.StaticTimer;

import java.util.HashMap;

public class SpritesComponent extends Component
{
	private final HashMap<String, SpriteSheet> spriteSheets;
	private SpriteSheet current = null;
	private int currentId = 0;
	private final StaticTimer timer;

	public SpritesComponent(GameObject relativeObject, Integer priority)
	{
		super(relativeObject, priority);
		this.spriteSheets = new HashMap<>();
		this.timer = new StaticTimer(100);
		this.timer.activate();
	}

	public void addSpriteSheet(String name, SpriteSheet spriteSheet)
	{
		this.spriteSheets.put(name, spriteSheet);
	}

	public void setCurrent(String name)
	{
		current = spriteSheets.get(name);
		getParent().setTexture(current.getTexture(), false);
		currentId = 0;
		getParent().setSprite(current.getSprite(0));
	}

	public void setCurrentId(int id)
	{
		currentId = id;
	}

	public void setSpeedAnimation(float delay)
	{
		this.timer.setMaxDelay(delay);
		this.timer.cancel();
		this.timer.activate();
	}

	@Override
	public void update()
	{
		if(timer.isFinished())
		{
			timer.activate();
			getParent().setSprite(current.getSprite(currentId));
			currentId = (currentId + 1) % current.getNbSprite();
		}
	}
}
