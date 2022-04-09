package com.geode.net.query;

import java.io.Serializable;
import java.util.Collections;

public class QueryBuilder
{
    private final StringMapper mapper;
    private GeodeQuery query;

    private QueryBuilder(StringMapper mapper)
    {
        this.mapper = mapper;
        clear();
    }

    public QueryBuilder clear()
    {
        query = new GeodeQuery();
        return this;
    }

    public QueryBuilder type(String type)
    {
        query.setType(mapper.get(type));
        return this;
    }

    public QueryBuilder args(Serializable ... arguments)
    {
        Collections.addAll(query.getArgs(), arguments);
        return this;
    }

    public GeodeQuery build()
    {
        GeodeQuery buffer = query;
        clear();
        return buffer;
    }

    public StringMapper getMapper()
    {
        return mapper;
    }

    public GeodeQuery getQuery()
    {
        return query;
    }
}
