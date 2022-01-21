package com.geode.engine.system;

import com.geode.engine.events.WindowCloseEvent;
import com.geode.engine.events.WindowEventsManager;
import com.geode.engine.exceptions.WindowException;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@Getter @Setter
public class Window
{
    public static final long NO_WINDOW = -1L;
    private static final Logger logger = LogManager.getLogger(Window.class);
    private static Window window;

    private Long id = NO_WINDOW;
    private Monitor monitor;
    private String title;
    private WindowEventsManager eventsManager;

    public static Window create(Vector2i size, String title, WindowHints hints) throws WindowException
    {
        return new Window(size, title, hints);
    }

    private Window(Vector2i size, String title, WindowHints hints) throws WindowException
    {
        setSize(size);
        setTitle(title);
        setMonitor(Monitor.getDefault());
        init(size, hints);
    }

    private void init(Vector2i size, WindowHints hints) throws WindowException
    {
        if(!isInitialized())
        {
            logger.info("glfw initialization");
            if(GLFW.glfwInit())
            {
                logger.info("glfw initialized");
                GLFWErrorCallback.createPrint(System.err).set();
                logger.info("glfw error callback set on error output");
                hints.hint(GLFW_CONTEXT_VERSION_MAJOR, 2)
                .hint(GLFW_CONTEXT_VERSION_MINOR, 0)
                .apply();
                setId(GLFW.glfwCreateWindow(size.x, size.y, title, monitor.getId(), NULL));
                if(isInitialized())
                {
                    setEventsManager(new WindowEventsManager(this));
                    logger.info("glfw window initialized");
                    Window.setWindow(this);
                }
                else
                {
                    logger.fatal("glfw window initialization failed");
                    throw new WindowException("glfw window initialization failed");
                }
            }
            else
            {
                logger.fatal("glfw initialization failed");
                throw new WindowException("GLFW can not be initialized");
            }
        }
    }



    public void makeCurrent()
    {
        GLFW.glfwMakeContextCurrent(getId());
        glfwSwapInterval(1);
    }

    public void setClearColor(Vector4f color)
    {
        glClearColor(color.x, color.y, color.z, color.w);
    }

    public boolean shouldClose()
    {
        return glfwWindowShouldClose(getId());
    }

    private static void setWindow(Window window) throws WindowException
    {
        if(Window.window != null)
            throw new WindowException("window object must be unique");
        Window.window = window;
    }

    public static Window getWindow()
    {
        return Window.window;
    }

    public void show()
    {
        glfwShowWindow(getId());
    }

    public void clear()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void flip()
    {
        glfwSwapBuffers(getId());
    }

    public void pollEvents()
    {
        glfwPollEvents();
    }

    public void destroy()
    {
        logger.info("destroying glfw window");
        glfwFreeCallbacks(getId());
        GLFW.glfwDestroyWindow(getId());
    }

    public void terminate()
    {
        logger.info("terminating glfw");
        GLFW.glfwTerminate();
    }

    public void shouldClose(boolean value)
    {
        glfwSetWindowShouldClose(getId(), value);
    }

    public void close()
    {
        destroy();
        terminate();
    }

    public boolean isInitialized()
    {
        return id != NO_WINDOW && id != NULL;
    }

    public void setSize(int width, int height)
    {
        glfwSetWindowSize(getId(), width, height);
    }

    public void setSize(Vector2i size)
    {
        setSize(size.x, size.y);
    }
}
