package com.geode.engine.events;

import com.geode.engine.system.Window;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WindowEventsManager
{
    @Getter
    private final WindowCloseEvent closeEvent;

    @Getter
    private final WindowSizeEvent sizeEvent;

    @Getter
    private final WindowFramebufferSizeEvent framebufferSizeEvent;

    @Getter
    private final WindowContentScaleEvent contentScaleEvent;

    @Getter
    private final WindowPosEvent posEvent;

    @Getter
    private final WindowIconifyEvent iconifyEvent;

    @Getter
    private final WindowMaximizeEvent maximizeEvent;

    @Getter
    private final WindowFocusEvent focusEvent;

    @Getter
    private final WindowRefreshEvent refreshEvent;

    @Getter
    private final WindowMonitorEvent monitorEvent;

    @Getter
    private final WindowKeyEvent keyEvent;

    @Getter
    private final WindowTextInputEvent textInputEvent;

    @Getter
    private final WindowCursorPosEvent cursorPosEvent;

    @Getter
    private final WindowCursorEnterEvent cursorEnterEvent;

    @Getter
    private final WindowMouseButtonEvent mouseButtonEvent;

    @Getter
    private final WindowScrollEvent scrollEvent;

    @Getter
    private final Window window;

    public WindowEventsManager(Window window)
    {
        this.window = window;
        closeEvent = new WindowCloseEvent(window.getId());
        sizeEvent = new WindowSizeEvent(window.getId());
        framebufferSizeEvent = new WindowFramebufferSizeEvent(window.getId());
        contentScaleEvent = new WindowContentScaleEvent(window.getId());
        posEvent = new WindowPosEvent(window.getId());
        iconifyEvent = new WindowIconifyEvent(window.getId());
        maximizeEvent = new WindowMaximizeEvent(window.getId());
        focusEvent = new WindowFocusEvent(window.getId());
        refreshEvent = new WindowRefreshEvent(window.getId());
        monitorEvent = new WindowMonitorEvent();
        keyEvent = new WindowKeyEvent(window.getId());
        textInputEvent = new WindowTextInputEvent(window.getId());
        cursorPosEvent = new WindowCursorPosEvent(window.getId());
        cursorEnterEvent = new WindowCursorEnterEvent(window.getId());
        mouseButtonEvent = new WindowMouseButtonEvent(window.getId());
        scrollEvent = new WindowScrollEvent(window.getId());
    }

    public void initObject(Object obj)
    {
        for(Method method : obj.getClass().getMethods())
        {
            if(method.isAnnotationPresent(WindowEvent.OnClose.class))
            {
                closeEvent.getCallbacks().add(() -> {try {method.invoke(obj);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnSize.class))
            {
                sizeEvent.getCallbacks().add((w, h) -> {try {method.invoke(obj, w, h);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnFramebufferSize.class))
            {
                framebufferSizeEvent.getCallbacks().add((w, h) -> {try {method.invoke(obj, w, h);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnContentScale.class))
            {
                contentScaleEvent.getCallbacks().add((xs, xy) -> {try {method.invoke(obj, xs, xy);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnPos.class))
            {
                posEvent.getCallbacks().add((x, y) -> {try {method.invoke(obj, x, y);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnIconify.class))
            {
                iconifyEvent.getCallbacks().add((ic) -> {try {method.invoke(obj, ic);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnMaximize.class))
            {
                maximizeEvent.getCallbacks().add((s) -> {try {method.invoke(obj, s);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnFocus.class))
            {
                focusEvent.getCallbacks().add((s) -> {try {method.invoke(obj, s);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnRefresh.class))
            {
                refreshEvent.getCallbacks().add(() -> {try {method.invoke(obj);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnMonitor.class))
            {
                monitorEvent.getCallbacks().add((state) -> {try {method.invoke(obj, state);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnKey.class))
            {
                keyEvent.getCallbacks().add((a, b, c, d) -> {try {method.invoke(obj, a, b, c, d);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnTextInput.class))
            {
                textInputEvent.getCallbacks().add((c) -> {try {method.invoke(obj, c);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnCursorPos.class))
            {
                cursorPosEvent.getCallbacks().add((x, y) -> {try {method.invoke(obj, x, y);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnCursorEnter.class))
            {
                cursorEnterEvent.getCallbacks().add((state) -> {try {method.invoke(obj, state);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnMouseButton.class))
            {
                mouseButtonEvent.getCallbacks().add((a, b, c) -> {try {method.invoke(obj, a, b, c);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
            else if(method.isAnnotationPresent(WindowEvent.OnScroll.class))
            {
                scrollEvent.getCallbacks().add((a, b) -> {try {method.invoke(obj, a, b);} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}});
            }
        }
    }
}
