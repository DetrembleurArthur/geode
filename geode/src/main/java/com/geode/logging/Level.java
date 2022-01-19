package com.geode.logging;

public enum Level
{
    DEBUG(0, "DEBUG"),
    INFO(1, "INFO"),
    WARNING(2, "WARNING"),
    ERROR(3, "ERROR"),
    CRITICAL(4, "CRITICAL"),
    FATAL(5, "FATAL");

    private int id;
    private String banner;

    Level(int id, String banner)
    {
        this.id = id;
        this.banner = banner;
    }

    public int getId()
    {
        return id;
    }

    public String getBanner()
    {
        return banner;
    }
}
