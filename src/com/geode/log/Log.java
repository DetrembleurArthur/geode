package com.geode.log;

import com.geode.net.GeodeIdentifiable;

import java.time.LocalDateTime;

public class Log
{
    public static void out(String who, String msg)
    {
        System.out.println("\033[96m[" + who + "]\033[0m \033[94m" + LocalDateTime.now().toString().replaceFirst("T", " at ") + "\033[0m => " + msg);
    }

    public static void err(String who, String msg)
    {
        System.err.println("[" + who + "] " + LocalDateTime.now().toString().replaceFirst("T", " at ") + " => " + msg);
    }

    public static void out(GeodeIdentifiable geodeIdentifiable, String msg)
    {
        out(geodeIdentifiable.getGeodeId(), msg);
    }

    public static void err(GeodeIdentifiable geodeIdentifiable, String msg)
    {
        err(geodeIdentifiable.getGeodeId(), msg);
    }
}
