package com.geode;

import com.geode.exceptions.GeodeException;
import com.geode.net.GeodeApplication;
import com.geode.net.Query;

public class Main
{
    public static void main(String[] args) throws GeodeException
    {
        GeodeApplication geodeApplication = new GeodeApplication("resources/geode.xml");
    }
}
