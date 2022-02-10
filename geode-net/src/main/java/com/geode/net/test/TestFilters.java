package com.geode.net.test;

import java.util.Random;

import com.geode.net.annotations.Filtering;
import com.geode.net.queries.GeodeQuery;

public class TestFilters
{
    @Filtering
    public boolean filter1(GeodeQuery geodeQuery)
    {
        return true;
    }
}
