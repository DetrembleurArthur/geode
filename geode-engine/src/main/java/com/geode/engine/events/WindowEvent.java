package com.geode.engine.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

@NoArgsConstructor
public abstract class WindowEvent<T>
{
    @Getter
    private final ArrayList<T> callbacks = new ArrayList<>();


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OnClose {}
}
