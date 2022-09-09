package com.geode.engine.core;

import com.geode.engine.events.WindowEventsManager;
import com.geode.engine.exceptions.WindowException;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@Getter
@Setter
public class Window
{
    public static final long NO_WINDOW = -1L;
    private static final Logger logger = LogManager.getLogger(Window.class);
    private static Window window;
    public static final Vector2i DEFAULT_SIZE = new Vector2i(16 * 100, 9 * 100);
    public static final String DEFAULT_TITLE = "Geode Application";

    private Long id = NO_WINDOW;
    private String title;
    private WindowEventsManager eventsManager;
    private Vector2i aspect;
    private float aspectRatio;
    private Runnable eventRunner;
    private Cursor cursor;
    private Vector2f viewportPos;
    private Vector2f viewportSize;

    public static void postEmptyEvent()
    {
        glfwPostEmptyEvent();
    }

    public static Window get(Vector2i size, String title, WindowHints hints) throws WindowException
    {
        return new Window(size, title, hints);
    }

    private Window(Vector2i size, String title, WindowHints hints) throws WindowException
    {
        setSize(size);
        setTitle(title);
        init(size, title, hints);
    }

    private void init(Vector2i size, String title, WindowHints hints) throws WindowException
    {
        if (!isInitialized())
        {
            logger.info("glfw initialization");
            if (GLFW.glfwInit())
            {
                logger.info("glfw initialized");
                GLFWErrorCallback.createPrint(System.err).set();
                logger.info("glfw error callback set on error output");
                hints.hint(GLFW_CONTEXT_VERSION_MAJOR, 2)
                        .hint(GLFW_CONTEXT_VERSION_MINOR, 0)
                        .apply();
                this.title = title;
                setId(GLFW.glfwCreateWindow(size.x, size.y, title, NULL, NULL));
                if (isInitialized())
                {
                    setEventsManager(new WindowEventsManager(this));
                    pollEventsPolicy();
                    aspectRatio = (float) size.x / size.y;
                    setAspect(size.x, size.y);
                    logger.info("glfw window initialized");
                    Window.setWindow(this);
                } else
                {
                    logger.fatal("glfw window initialization failed");
                    throw new WindowException("glfw window initialization failed");
                }
            } else
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
        if (Window.window != null)
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

    public void hide()
    {
        glfwHideWindow(getId());
    }

    public void clear()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void flip()
    {
        glfwSwapBuffers(getId());
    }

    private void pollEvents()
    {
        glfwPollEvents();
    }

    private void waitEvents()
    {
        glfwWaitEvents();
    }

    private void waitEventsTimeout(float sec)
    {
        glfwWaitEventsTimeout(sec);
    }

    public void pollEventsPolicy()
    {
        eventRunner = this::pollEvents;
    }

    public void waitEventsPolicy()
    {
        eventRunner = this::waitEvents;
    }

    public void waitEventsTimeoutPolicy(float sec)
    {
        eventRunner = () -> waitEventsTimeout(sec);
    }

    public void checkEvents()
    {
        eventRunner.run();
    }

    public void destroy()
    {
        logger.info("destroying glfw window");
        if (cursor != null)
            cursor.destroy();
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
        aspectRatio = (float) width / height;
    }

    public void setSize(Vector2i size)
    {
        setSize(size.x, size.y);
    }

    public Vector2i getSize()
    {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetWindowSize(getId(), width, height);
        return new Vector2i(width[0], height[0]);
    }

    public Vector2f getSizef()
    {
        Vector2i size = getSize();
        return new Vector2f(size.x, size.y);
    }

    public Vector4i getFrameSize()
    {
        int[] left = new int[1];
        int[] top = new int[1];
        int[] right = new int[1];
        int[] bottom = new int[1];
        glfwGetWindowFrameSize(getId(), left, top, right, bottom);
        return new Vector4i(left[0], top[0], right[0], bottom[0]);
    }

    public Vector2i getFramebufferSize()
    {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetFramebufferSize(getId(), width, height);
        return new Vector2i(width[0], height[0]);
    }

    public Vector2f getContentScale()
    {
        float[] xscale = new float[1];
        float[] yscale = new float[1];
        glfwGetWindowContentScale(getId(), xscale, yscale);
        return new Vector2f(xscale[0], yscale[0]);
    }

    public void setSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight)
    {
        glfwSetWindowSizeLimits(getId(), minWidth, minHeight, maxWidth, maxHeight);
    }

    public void disableSizeLimits()
    {
        setSizeLimits(GLFW_DONT_CARE, GLFW_DONT_CARE, GLFW_DONT_CARE, GLFW_DONT_CARE);
    }

    public void setAspect(int numer, int denom)
    {
        if (aspect != null) return;
        glfwSetWindowAspectRatio(getId(), numer, denom);
        aspect = new Vector2i(numer, denom);
    }

    public void setPosition(int x, int y)
    {
        glfwSetWindowPos(getId(), x, y);
    }

    public void setPosition(Vector2i position)
    {
        setPosition(position.x, position.y);
    }

    public void center()
    {
        Vector2i monitorPosition = Monitor.getSelected().getPosition();
        Vector2i monitorSize = Monitor.getSelected().getSize();
        Vector2i windowSize = getSize();
        setPosition(monitorPosition.add(monitorSize.div(2).sub(windowSize.div(2))));
    }

    public Vector2i getPosition()
    {
        int[] x = new int[1];
        int[] y = new int[1];
        glfwGetWindowPos(getId(), x, y);
        return new Vector2i(x[0], y[0]);
    }

    public void setTitle(String title)
    {
        glfwSetWindowTitle(getId(), title);
        this.title = title;
    }

    public Monitor getAssociatedMonitor()
    {
        return new Monitor(glfwGetWindowMonitor(getId()));
    }

    public void setAssociatedMonitor(Monitor monitor)
    {
        GLFWVidMode vidMode = monitor.getVideoMode();
        glfwSetWindowMonitor(getId(), monitor.getId(), 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
    }

    public void iconify()
    {
        glfwIconifyWindow(getId());
    }

    public void maximize()
    {
        glfwMaximizeWindow(getId());
    }

    public void restore()
    {
        glfwRestoreWindow(getId());
    }

    public void minimize()
    {
        restore();
    }

    public boolean isIconified()
    {
        return glfwGetWindowAttrib(getId(), GLFW_ICONIFIED) == 1;
    }

    public boolean isMaximized()
    {
        return glfwGetWindowAttrib(getId(), GLFW_MAXIMIZED) == 1;
    }

    public boolean isVisible()
    {
        return glfwGetWindowAttrib(getId(), GLFW_VISIBLE) == 1;
    }

    public void focus()
    {
        glfwFocusWindow(getId());
    }

    public boolean isFocused()
    {
        return glfwGetWindowAttrib(getId(), GLFW_FOCUSED) == 1;
    }

    public void requestAttention()
    {
        glfwRequestWindowAttention(getId());
    }

    public boolean isFrameBufferTransparent()
    {
        return glfwGetWindowAttrib(getId(), GLFW_TRANSPARENT_FRAMEBUFFER) == 1;
    }

    public void setOpacity(float opacity)
    {
        if (opacity >= 0f && opacity <= 1f)
        {
            glfwSetWindowOpacity(getId(), opacity);
        }
    }

    public float getOpacity()
    {
        return glfwGetWindowOpacity(getId());
    }

    public boolean isHovered()
    {
        return glfwGetWindowAttrib(getId(), GLFW_HOVERED) == 1;
    }

    public Vector2f getCenter()
    {
        return getSizef().div(2);
    }

    public void setCursor(Cursor cursor)
    {
        if (this.cursor != null)
            this.cursor.destroy();
        glfwSetCursor(getId(), cursor.getId());
        this.cursor = cursor;
    }

    public void setDefaultCursor()
    {
        if (this.cursor != null)
            this.cursor.destroy();
        glfwSetCursor(getId(), NULL);
    }

    public void applyViewport(int x, int y, int w, int h)
    {
        glViewport(x, y, w, h);
        viewportPos = new Vector2f(x, y);
        viewportSize = new Vector2f(w, h);
    }

    public void fullscreen()
    {
        setAssociatedMonitor(Monitor.getSelected());
    }

    public void noFullscreen()
    {
        Vector2i size = getSize().div(2);
        glfwSetWindowMonitor(getId(), NULL, 0, 0, size.x, size.y, 0);
        center();
    }

    @Deprecated
    public void setIcon(String iconFilepath)
    {
        //glfwSetWindowIcon(getId(), new GLFWImage.Buffer(ByteBuffer.wrap(pixels)));
    }
}
