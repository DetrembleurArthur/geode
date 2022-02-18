package com.geode.net.filters;

import java.util.ArrayList;

import com.geode.net.queries.GeodeQuery;
import com.geode.net.queries.GeodeQuery.Category;

public interface Filter
{
    boolean evaluate(GeodeQuery query);

    static Filter createCategoryFilter(ArrayList<Category> categories)
    {
        return query -> !categories.contains(query.getCategory());
    }
}
