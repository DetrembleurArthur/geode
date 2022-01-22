package com.geode.engine.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.annotation.*;
import java.util.ArrayList;

@NoArgsConstructor
public abstract class WindowEvent<T>
{
    @Getter
    private final ArrayList<T> callbacks = new ArrayList<>();

    public WindowEvent(long windowId)
    {
        register(windowId);
    }

    protected abstract void register(long windowId);

    public void remove(T callback)
    {
        if(callback != null)
            callbacks.remove(callback);
        else
            callbacks.clear();
    }


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OnClose {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OnSize {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OnFramebufferSize {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OnContentScale {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OnPos {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OnIconify {}
}
