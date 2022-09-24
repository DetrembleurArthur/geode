package com.geode.engine.entity.components.event;

import com.geode.engine.core.MouseManager;
import com.geode.engine.entity.GameObject;
import com.geode.engine.graphics.Camera;
import org.joml.Vector2f;

public class MouseButtonClickEvent extends MouseButtonEvent
{
    private boolean clickBloqued;
    private boolean clicked;
    private boolean repeated;

    public MouseButtonClickEvent(GameObject relativeObject, boolean repeated, Camera camera2D)
    {
        this(relativeObject, -1, repeated, camera2D);
    }

    public MouseButtonClickEvent(GameObject relativeObject, int buttonId, boolean repeated, Camera camera2D)
    {
        super(relativeObject, buttonId, camera2D);
        clickBloqued = false;
        clicked = false;
        this.repeated = repeated;
    }

    @Override
    boolean isAppend()
    {
        if ((buttonId == -1 && MouseManager.get().isButtonPressed()) || (buttonId != -1 && MouseManager.get().isPressed(buttonId)))
        {
            Vector2f mousePosition = MouseManager.getMousePositionf(camera);
            if (((GameObject)object).collision_c().contains(mousePosition) || clicked)
            {
                if (!clickBloqued)
                {
                    clickBloqued = !repeated;
                    clicked = true;
                    return true;
                }
            } else
            {
                clickBloqued = true;
            }
        } else
        {
            if (clickBloqued)
            {
                clickBloqued = false;
            }
            clicked = false;
        }
        return false;
    }

    public boolean isClickBloqued()
    {
        return clickBloqued;
    }

    public boolean isRepeated()
    {
        return repeated;
    }

    public boolean isClicked()
    {
        return clicked;
    }
}
