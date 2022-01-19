package com.geode.net.net;

public class SettingsInfos
{
    private boolean enableLogging = true;
    private boolean appendLogging = false;
    private String loggingFile = null;
    
    public boolean isEnableLogging() {
        return enableLogging;
    }
    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }
    public boolean isAppendLogging() {
        return appendLogging;
    }
    public void setAppendLogging(boolean appendLogging) {
        this.appendLogging = appendLogging;
    }
    public String getLoggingFile() {
        return loggingFile;
    }
    public void setLoggingFile(String loggingFile) {
        this.loggingFile = loggingFile;
    }
    @Override
    public String toString() {
        return "SettingsInfos [appendLogging=" + appendLogging + ", enableLogging=" + enableLogging + ", loggingFile="
                + loggingFile + "]";
    }
}
