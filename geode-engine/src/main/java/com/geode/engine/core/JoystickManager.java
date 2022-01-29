package com.geode.engine.core;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class JoystickManager
{
    public static class Xbox
    {
        public static final int A = 0;
        public static final int B = 1;
        public static final int X = 2;
        public static final int Y = 3;
        public static final int LB = 4;
        public static final int RB = 5;
        public static final int SELECT = 6;
        public static final int START = 7;
        public static final int L = 8;
        public static final int R = 9;
        public static final int UP = 10;
        public static final int RIGHT = 11;
        public static final int DOWN = 12;
        public static final int LEFT = 13;
    }

    public static boolean isPresent(int jid)
    {
        return GLFW.glfwJoystickPresent(jid);
    }

    public static boolean isGamepad(int jid)
    {
        return GLFW.glfwJoystickIsGamepad(jid);
    }

    public static Vector2f getAxes(int jid, int j)
    {
        FloatBuffer buffer = GLFW.glfwGetJoystickAxes(jid);
        assert buffer != null;
        return new Vector2f(buffer.get(j*2), buffer.get(j*2+1));
    }

    public static boolean isPressed(int jid, int btn)
    {
        ByteBuffer buffer = GLFW.glfwGetJoystickButtons(jid);
        assert buffer != null;
        return buffer.get(btn) == MouseManager.Button.PRESSED;
    }

    public static boolean isReleased(int jid, int btn)
    {
        ByteBuffer buffer = GLFW.glfwGetJoystickButtons(jid);
        assert buffer != null;
        return buffer.get(btn) == MouseManager.Button.RELEASED;
    }

    public static boolean isHatsPressed(int jid, int hat)
    {
        ByteBuffer buffer = GLFW.glfwGetJoystickHats(jid);
        assert buffer != null;
        return (buffer.get() & hat) == hat;
    }

    public static String getName(int jid)
    {
        return GLFW.glfwGetJoystickName(jid);
    }

    public static String getGamepadName(int jid)
    {
        return GLFW.glfwGetGamepadName(jid);
    }

    private interface GamepadManager<T>
    {
        T manage(GLFWGamepadState state);
    }

    private static <T> T manageGamepadState(int jid, GamepadManager<T> gamepadManager)
    {
        try(GLFWGamepadState state = GLFWGamepadState.malloc())
        {
            if(GLFW.glfwGetGamepadState(jid, state))
            {
                return gamepadManager.manage(state);
            }
        }
        return null;
    }

    public static Vector2f getGamepadAxes(int jid, int j)
    {
        FloatBuffer buffer = manageGamepadState(jid, GLFWGamepadState::axes);
        assert buffer != null;
        return new Vector2f(buffer.get(j*2), buffer.get(j*2 + 1));
    }

    public static boolean isGamepadButtonPressed(int jid, int btn)
    {
        return Boolean.TRUE.equals(manageGamepadState(jid, state -> state.buttons(btn) != 0));
    }
}
