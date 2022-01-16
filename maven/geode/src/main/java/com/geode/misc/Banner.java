package com.geode.misc;

public class Banner
{
    public static void show(String banner)
    {
        int len = "=========================".length() - banner.length();
        System.out.println("\n+=========================+");
        System.out.print("|");
        for(int i = 0; i < len / 2; i++)
            System.out.print(" ");
        System.out.print(banner);
        for(int i = 0; i < len / 2; i++)
            System.out.print(" ");
        if(banner.length() % 2 == 0) System.out.print(" ");
        System.out.println("|");
        System.out.println("+=========================+\n");
    }
}
