package com.geode.engine.entity.components.script;

public interface ScriptPolicy<T>
{
    boolean granted(Script<T> script);
}
