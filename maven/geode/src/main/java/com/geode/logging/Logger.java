package com.geode.logging;

import java.io.*;

public class Logger
{
    private static Level level = Level.DEBUG;
    private static boolean cmdOut = true;
    private static FileWriter fileWriter;

    private Class<?> loggingClassScope;

    public Logger(Class<?> loggingClassScope)
    {
        this.loggingClassScope = loggingClassScope;
    }

    public void write(String message, Level level)
    {
        if(level.getId() >= Logger.level.getId())
        {
            StringBuilder builder = new StringBuilder();
            builder.append("[").append(level.getBanner()).append("] : ").append(loggingClassScope.getCanonicalName()).append(" : ").append(message);
            if(cmdOut) System.err.println(builder);
            if(fileWriter != null)
            {
                try
                {
                    fileWriter.write(builder.toString() + "\n");
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

    public static void setLevel(Level level)
    {
        Logger.level = level;
    }

    public static void setCmdOut(boolean cmdOut)
    {
        Logger.cmdOut = cmdOut;
    }

    public static void setFile(String pathFile)
    {
        try
        {
            Logger.fileWriter = new FileWriter(new File(pathFile));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
