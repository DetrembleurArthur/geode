package com.geode.net.info;

import java.util.ArrayList;

import com.geode.net.annotations.Attribute;
import com.geode.net.queries.GeodeQuery.Category;

public class FiltersInfosBuilder extends Builder<FiltersInfos>
{
    static
    {
        BuildersMap.register(FiltersInfos.class, FiltersInfosBuilder.class);
    }

    public FiltersInfosBuilder()
    {
        reset();
    }

    public static FiltersInfosBuilder create()
    {
        return new FiltersInfosBuilder();
    }

    @Override
    public FiltersInfosBuilder reset()
    {
        object = new FiltersInfos();
        return this;
    }

    @Attribute("allow-query-categories")
    public FiltersInfosBuilder allowCategories(ArrayList<String> categories)
    {
        ArrayList<Category> categories2 = new ArrayList<>();
        categories.forEach(c -> categories2.remove(Category.valueOf(c.toUpperCase())));
        object.setQueryCategory(categories2);
        return this;
    }

    @Attribute("bundle")
    public FiltersInfosBuilder bundle(String bundleClassString)
    {
        try {
            object.setBundle(Class.forName(bundleClassString));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }
}
