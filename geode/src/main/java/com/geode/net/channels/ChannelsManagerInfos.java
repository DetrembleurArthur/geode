package com.geode.net.channels;

public class ChannelsManagerInfos
{
    private boolean enable = true;
    private boolean strict = true;

    public boolean isEnable() {
        return enable;
    }
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    public boolean isStrict() {
        return strict;
    }
    public void setStrict(boolean strict) {
        this.strict = strict;
    }
    @Override
    public String toString() {
        return "ChannelsManagerInfos [enable=" + enable + ", strict=" + strict + "]";
    }

    
}
