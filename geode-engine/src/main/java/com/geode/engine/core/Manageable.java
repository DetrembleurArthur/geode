package com.geode.engine.core;

public interface Manageable
{
    void load();
    void update(float dt);
    void destroy();
}
