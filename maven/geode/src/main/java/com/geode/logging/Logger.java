package com.geode.logging;

import java.io.*;
import java.time.Instant;
import java.util.Date;

public class Logger
{
    private static Level level = Level.DEBUG;
    private static boolean cmdOut = true;
    private static FileWriter fileWriter;

    private final Class<?> loggingClassScope;

    public Logger(Class<?> loggingClassScope)
    {
        this.loggingClassScope = loggingClassScope;
    }

    public void write(String message, Level level)
    {
        if(level.getId() >= Logger.level.getId())
        {
            StringBuilder builder = new StringBuilder();
            builder.append("[")
                    .append(level.getBanner())
                    .append("] : th(")
                    .append(Thread.currentThread().getId())
                    .append(") : ")
                    .append(Date.from(Instant.now()))
                    .append(" : ")
                    .append(loggingClassScope.getName())
                    .append(" : ")
                    .append(message);
            if(cmdOut)
            {
                if(level.getId() >= Level.WARNING.getId())
                    System.err.println(builder);
                else
                    System.out.println(builder);
            }
            if(fileWriter != null)
            {
                try
                {
                    fileWriter.write(builder + "\n");
                    fileWriter.flush();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void debug(String message)
    {
        write(message, Level.DEBUG);
    }

    public void info(String message)
    {
        write(message, Level.INFO);
    }

    public void warning(String message)
    {
        write(message, Level.WARNING);
    }

    public void error(String message)
    {
        write(message, Level.ERROR);
    }

    public void critical(String message)
    {
        write(message, Level.CRITICAL);
    }

    public void fatal(String message)
    {
        write(message, Level.FATAL);
    }

    public void debug(String message, String who)
    {
        write(message + " : " + who, Level.DEBUG);
    }

    public void info(String message, String who)
    {
        write(message + " : " + who, Level.INFO);
    }

    public void warning(String message, String who)
    {
        write(message + " : " + who, Level.WARNING);
    }

    public void error(String message, String who)
    {
        write(message + " : " + who, Level.ERROR);
    }

    public void critical(String message, String who)
    {
        write(message + " : " + who, Level.CRITICAL);
    }

    public void fatal(String message, String who)
    {
        write(message + " : " + who, Level.FATAL);
    }

    public static void setLevel(Level level)
    {
        Logger.level = level;
    }

    public static void setCmdOut(boolean cmdOut)
    {
        Logger.cmdOut = cmdOut;
    }

    public static void setFile(String pathFile, boolean append)
    {
        try
        {
            Logger.fileWriter = new FileWriter(new File(pathFile), append);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void setFile(String pathFile)
    {
        Logger.setFile(pathFile, false);
    }
}
