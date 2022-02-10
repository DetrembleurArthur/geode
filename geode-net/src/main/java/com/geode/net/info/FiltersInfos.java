package com.geode.net.info;

import java.util.ArrayList;

import com.geode.net.queries.GeodeQuery.Category;

public class FiltersInfos
{
    private ArrayList<Category> queryCategories;
    private Class<?> bundle;

    public FiltersInfos()
    {
        queryCategories = new ArrayList<>();
        for(Category category : Category.values())
            queryCategories.add(category);
    }

    public ArrayList<Category> getQueryCategories()
    {
        return queryCategories;
    }

    public void setQueryCategory(ArrayList<Category> queryCategories)
    {
        this.queryCategories = queryCategories;
    }

    public Class<?> getBundle() {
        return bundle;
    }

    public void setBundle(Class<?> bundle) {
        this.bundle = bundle;
    }
}
