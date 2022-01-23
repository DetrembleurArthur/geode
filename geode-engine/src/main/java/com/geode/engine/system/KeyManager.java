package com.geode.engine.system;

import com.geode.engine.events.WindowKeyI;
import lombok.Builder;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

public class KeyManager implements WindowKeyI
{
    public static class Mods
    {
        public static final int SHIFT = GLFW.GLFW_MOD_SHIFT;
        public static final int CONTROL = GLFW.GLFW_MOD_CONTROL;
        public static final int ALT = GLFW.GLFW_MOD_ALT;
        public static final int SUPER = GLFW.GLFW_MOD_SUPER;
        public static final int CAPS_LOCK = GLFW.GLFW_MOD_CAPS_LOCK;
        public static final int NUM_LOCK = GLFW.GLFW_MOD_NUM_LOCK;
    }

    @Builder
    public static class Key
    {
        private int code;
        private int action;
        private int mode;

        public static final Key NULL_KEY = new Key(-1, -1, 0);
    }

    public static KeyManager keyManager;

    private static Key.KeyBuilder builder = Key.builder();

    private Key[] keys;

    private KeyManager()
    {
        keys = new Key[GLFW.GLFW_KEY_LAST];
    }

    public static KeyManager create()
    {
        if(keyManager == null)
            keyManager = new KeyManager();
        return keyManager;
    }

    @Override
    public void onKey(int key, int scancode, int action, int mods)
    {
        keys[scancode] = builder.code(key).action(action).mode(mods).build();
    }

    public Key getKey(int key)
    {
        Key key1 = keys[getScanCode(key)];
        return key1 != null ? key1 : Key.NULL_KEY;
    }

    public boolean isKeyAction(int key, int action)
    {
        return getKey(key).action == action;
    }

    public boolean isKeyPressed(int key)
    {
        boolean state = isKeyAction(key, GLFW.GLFW_PRESS);
        if(state)
        {
            getKey(key).action = 0;
        }
        return state;
    }

    public boolean isKeyReleased(int key)
    {
        boolean state = isKeyAction(key, GLFW.GLFW_RELEASE);
        if(state)
        {
            getKey(key).action = -1;
        }
        return state;
    }

    public boolean isKeyRepeated(int key)
    {
        return isKeyAction(key, GLFW.GLFW_REPEAT);
    }

    public boolean isKeyMode(int key, int modes)
    {
        return (isKeyPressed(key) && (getKey(key).mode & modes) >= modes);
    }

    public boolean isKeyModeRepeated(int key, int modes)
    {
        return ((isKeyPressed(key) || isKeyRepeated(key)) && (getKey(key).mode & modes) >= modes);
    }

    public static int getScanCode(int key)
    {
        return GLFW.glfwGetKeyScancode(key);
    }

    public static int getStateKey(int key)
    {
        return GLFW.glfwGetKey(Window.getWindow().getId(), key);
    }

    public static String getChar(int key)
    {
        return GLFW.glfwGetKeyName(key, getScanCode(key));
    }

    public static void setStickyKeysMode()
    {
        GLFW.glfwSetInputMode(Window.getWindow().getId(), GLFW.GLFW_STICKY_KEYS, GLFW.GLFW_TRUE);
    }

    public static void setLockKeysMode(boolean state)
    {
        GLFW.glfwSetInputMode(Window.getWindow().getId(), GLFW.GLFW_LOCK_KEY_MODS, state ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
}
