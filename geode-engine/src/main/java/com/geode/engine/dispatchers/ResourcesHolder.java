package com.geode.engine.dispatchers;

import com.geode.engine.graphics.Shader;
import com.geode.engine.graphics.Texture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ResourcesHolder
{
    private static final Logger logger = LogManager.getLogger(ResourcesHolder.class);

    private final ArrayList<Resource> resources;
    
    public ResourcesHolder()
    {
        resources = new ArrayList<>();
    }

    public void dispatch(Object repository)
    {
        try
        {
            logger.info("dispatch resources through " + repository.getClass().getName());
            Field[] fields = repository.getClass().getFields();
            for(Field field : fields)
            {
                if(field.isAnnotationPresent(ResourceRef.class))
                {
                    ResourceRef resourceRef = field.getAnnotation(ResourceRef.class);
                    Resource resource = createResource(field, resourceRef);
                    resources.add(resource);
                    field.set(repository, resource);
                    assert resource != null;
                    logger.info(resource.getClass().getSimpleName() + " resource added");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private Resource createResource(Field field, ResourceRef resourceRef)
    {
        String name = resourceRef.value().isBlank() ? field.getName() : resourceRef.value();
        if(Texture.class.isAssignableFrom(field.getType()))
            return new Texture(name);
        else if(Shader.class.isAssignableFrom(field.getType()))
            return new Shader(name + ".vertex.glsl", name + ".fragment.glsl", true);
        return null;
    }
    
    public void destroy()
    {
        logger.info("destroy resources");
        resources.forEach(Resource::destroy);
        resources.clear();
    }
}
