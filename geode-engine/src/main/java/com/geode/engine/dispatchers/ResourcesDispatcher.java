package com.geode.engine.dispatchers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class ResourcesDispatcher
{
    private static final Logger logger = LogManager.getLogger(ResourcesDispatcher.class);

    private final HashMap<Object, ArrayList<ResourcesHolder>> holdersMap;

    public ResourcesDispatcher()
    {
        holdersMap = new HashMap<>();
    }

    public void dispatch(Object target)
    {
        try
        {
            ArrayList<ResourcesHolder> resourcesHolders = new ArrayList<>();
            holdersMap.put(target, resourcesHolders);
            Field[] fields = target.getClass().getFields();
            for (Field field : fields)
            {
                if (field.isAnnotationPresent(Repository.class))
                {
                    ResourcesHolder holder = new ResourcesHolder();
                    resourcesHolders.add(holder);
                    Object repository = field.getType().getConstructor().newInstance();
                    holder.dispatch(repository);
                    field.set(target, repository);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void destroy(Object repository)
    {
        holdersMap.get(repository).forEach(ResourcesHolder::destroy);
        holdersMap.get(repository).clear();
        holdersMap.remove(repository);
    }
}
