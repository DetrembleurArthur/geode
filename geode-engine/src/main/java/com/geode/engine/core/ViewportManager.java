package com.geode.engine.core;

import com.geode.engine.events.WindowSizeI;

public class ViewportManager implements WindowSizeI
{
    @Override
    public void onWindowSize(int w, int h)
    {
        Window window = Window.getWindow();
        int aspectWidth = w;
        int aspectHeight = (int)((float)aspectWidth / window.getAspectRatio());
        if(aspectHeight > h)
        {
            aspectHeight = h;
            aspectWidth = (int)((float)aspectHeight * window.getAspectRatio());
        }
        int vpx = (int)((w / 2f) - ((float)aspectWidth / 2f));
        int vpy = (int)((h / 2f) - ((float)aspectHeight / 2f));
        window.applyViewport(vpx, vpy, aspectWidth, aspectHeight);
    }
}
