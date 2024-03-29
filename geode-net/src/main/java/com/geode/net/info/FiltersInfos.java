package com.geode.net.info;

import java.util.ArrayList;
import java.util.Arrays;

import com.geode.net.queries.GeodeQuery.Category;

public class FiltersInfos
{
    private ArrayList<Category> queryCategories;
    private Class<?> bundle;
    private boolean checksum;

    public FiltersInfos()
    {
        queryCategories = new ArrayList<>();
        queryCategories.addAll(Arrays.asList(Category.values()));
        checksum = false;
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

    public boolean isChecksum()
    {
        return checksum;
    }

    public void setChecksum(boolean checksum)
    {
        this.checksum = checksum;
    }
}
