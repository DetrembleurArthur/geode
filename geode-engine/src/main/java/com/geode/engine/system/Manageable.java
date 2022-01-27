package com.geode.engine.system;

public interface Manageable
{
    void load();
    void update(float dt);
    void draw(Window window);
    void destroy();
}
