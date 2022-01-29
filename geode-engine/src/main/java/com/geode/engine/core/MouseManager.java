package com.geode.engine.core;

import com.geode.engine.events.WindowMouseButtonI;
import com.geode.engine.graphics.Camera;
import com.geode.engine.utils.MathUtils;
import lombok.Builder;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public class MouseManager implements WindowMouseButtonI
{
    @Getter
    private static MouseManager mouseManager;
    private Button[] buttons;

    @Builder
    static class Button
    {
        private int action;
        private int mods;

        public static final int PRESSED = GLFW_PRESS;
        public static final int RELEASED = GLFW_RELEASE;

        public static final Button NULL_BUTTON = Button.builder().mods(-1).action(-1).build();
    }

    public static MouseManager get()
    {
        if(mouseManager == null)
            MouseManager.mouseManager = new MouseManager();
        return mouseManager;
    }

    private MouseManager()
    {
        buttons = new Button[GLFW_MOUSE_BUTTON_LAST];
    }

    public Button getButton(int button)
    {
        Button button1 = buttons[button];
        return button1 != null ? button1 : Button.NULL_BUTTON;
    }

    public boolean isAction(int button, int action)
    {
        Button button1 = getButton(button);
        return button1.action == action;
    }

    public boolean isPressed(int button)
    {
        return isAction(button, Button.PRESSED);
    }

    public boolean isReleased(int button)
    {
        boolean state = isAction(button, Button.RELEASED);
        if(state)
        {
            getButton(button).action = -1;
        }
        return state;
    }

    public boolean isLeftButtonPressed()
    {
        return isPressed(GLFW_MOUSE_BUTTON_LEFT);
    }

    public boolean isRightButtonPressed()
    {
        return isPressed(GLFW_MOUSE_BUTTON_RIGHT);
    }

    public boolean isMiddleButtonPressed()
    {
        return isPressed(GLFW_MOUSE_BUTTON_MIDDLE);
    }

    public boolean isLeftButtonReleased()
    {
        return isReleased(GLFW_MOUSE_BUTTON_LEFT);
    }

    public boolean isRightButtonReleased()
    {
        return isReleased(GLFW_MOUSE_BUTTON_RIGHT);
    }

    public boolean isMiddleButtonReleased()
    {
        return isReleased(GLFW_MOUSE_BUTTON_MIDDLE);
    }

    public void disableAction(int button)
    {
        getButton(button).action = -1;
    }

    public void disableLeftButtonAction()
    {
        disableAction(GLFW_MOUSE_BUTTON_LEFT);
    }

    public void disableRightButtonAction()
    {
        disableAction(GLFW_MOUSE_BUTTON_RIGHT);
    }

    public void disableMiddleButtonAction()
    {
        disableAction(GLFW_MOUSE_BUTTON_MIDDLE);
    }

    @Override
    public void onMouseButton(int button, int action, int mods)
    {
        buttons[button] = Button.builder().action(action).mods(mods).build();
    }

    public static void disableCursor()
    {
        GLFW.glfwSetInputMode(Window.getWindow().getId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }

    public static void hideCursor()
    {
        GLFW.glfwSetInputMode(Window.getWindow().getId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
    }

    public static void showCursor()
    {
        GLFW.glfwSetInputMode(Window.getWindow().getId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }

    public static void setRawMouseMotion(boolean state)
    {
        if(glfwRawMouseMotionSupported())
            GLFW.glfwSetInputMode(Window.getWindow().getId(), GLFW_RAW_MOUSE_MOTION, state ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static void setStickyMouse(boolean state)
    {
        glfwSetInputMode(Window.getWindow().getId(), GLFW_STICKY_MOUSE_BUTTONS, state ? GLFW_TRUE : GLFW_FALSE);
    }

    public static Vector2i getMousePosition()
    {
        double[] x = new double[1];
        double[] y = new double[1];
        glfwGetCursorPos(Window.getWindow().getId(), x, y);
        return new Vector2i((int)x[0], (int)y[0]);
    }

    public static Vector2i getMousePosition(Camera camera)
    {
        Vector2i mp = getMousePosition();
        Vector2f mpf = MathUtils.screenToWorld(new Vector2f(mp.x, mp.y), camera);
        return new Vector2i((int)mpf.x, (int)mpf.y);
    }

    public static Vector2f getMousePositionf(Camera camera)
    {
        Vector2i mp = getMousePosition();
        Vector2f mpf = MathUtils.screenToWorld(new Vector2f(mp.x, mp.y), camera);
        return mpf;
    }
}
