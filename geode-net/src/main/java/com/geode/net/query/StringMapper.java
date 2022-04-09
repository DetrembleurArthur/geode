package com.geode.net.query;

import java.util.HashMap;

public class StringMapper
{
    private final HashMap<String, Long> stringToLong = new HashMap<>();
    private final HashMap<Long, String> longToString = new HashMap<>();

    public StringMapper()
    {
    }

    public void put(String ids, Long idl)
    {
        stringToLong.put(ids, idl);
        longToString.put(idl, ids);
    }

    public void put(Long idl, String ids)
    {
        put(ids, idl);
    }

    public String get(Long idl)
    {
        return longToString.get(idl);
    }

    public Long get(String ids)
    {
        return stringToLong.get(ids);
    }
}
