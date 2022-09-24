package com.geode.engine.entity.components.event;


import com.geode.engine.core.MouseManager;
import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.components.property.PropertyComponent;
import com.geode.engine.graphics.Camera;
import org.joml.Vector2f;

public class MouseButtonDragEvent extends MouseLeftButtonClickEvent
{
	public static class Orientation
	{
		public final static int BOTH = 0;
		public final static int HORIZONTAL = 1;
		public final static int VERTICAL = 2;
	}

	private static GameObject DRAG_FOCUSED = null;

	private Vector2f relativeVector;

	public MouseButtonDragEvent(GameObject relativeObject, Camera camera2D)
	{
		super(relativeObject, true, camera2D);
		relativeVector = new Vector2f();
	}

	@Override
	boolean isAppend()
	{
		boolean clicked = isClicked();
		boolean ret = super.isAppend();
		if(ret)
		{
			var mousePosition = MouseManager.getMousePositionf(camera);
			if(!clicked)
			{
				relativeVector = mousePosition.sub(((GameObject)object).getTransform().getPosition2D());
			}
			if(DRAG_FOCUSED == null)
				DRAG_FOCUSED = (GameObject) getObject();
			return true;
		}
		if(DRAG_FOCUSED == getObject() || (DRAG_FOCUSED != null && DRAG_FOCUSED.getMesh() == null))
			DRAG_FOCUSED = null;
		return false;
	}

	public Vector2f getRelativeVector()
	{
		return relativeVector;
	}

	public void setDragActionEvent(int orientation)
	{
		switch (orientation)
		{
			case Orientation.BOTH:
				setBothDragActionEvent();
				break;

			case Orientation.HORIZONTAL:
				setHorizontalDragActionEvent();
				break;

			case Orientation.VERTICAL:
				setVerticalDragActionEvent();
				break;
		}
	}

	public void setBothDragActionEvent()
	{
		PropertyComponent properties = ((GameObject)object).getComponent(PropertyComponent.class);
		addActionEvent(properties != null ?
				sender -> {if(dragAvailable())properties.position2D().set(MouseManager.getMousePositionf(camera).sub(relativeVector));} :
				sender -> {if(dragAvailable())((GameObject)object).getTransform().setPosition2D(MouseManager.getMousePositionf(camera).sub(relativeVector));});
	}

	public void setHorizontalDragActionEvent()
	{
		PropertyComponent properties = ((GameObject)object).getComponent(PropertyComponent.class);
		addActionEvent(properties != null ?
				sender -> {if(dragAvailable())properties.x().set(MouseManager.getMousePositionf(camera).x - relativeVector.x);}:
				sender -> {if(dragAvailable())((GameObject)object).getTransform().setX(MouseManager.getMousePositionf(camera).x - relativeVector.x);});
	}

	public void setVerticalDragActionEvent()
	{
		PropertyComponent properties = ((GameObject)object).getComponent(PropertyComponent.class);
		addActionEvent(properties != null ?
				sender -> {if(dragAvailable())properties.y().set(MouseManager.getMousePositionf(camera).y - relativeVector.y);}:
				sender -> {if(dragAvailable())((GameObject)object).getTransform().setY(MouseManager.getMousePositionf(camera).y - relativeVector.y);});
	}

	private boolean dragAvailable()
	{
		return DRAG_FOCUSED == null || DRAG_FOCUSED == getObject();
	}
}
