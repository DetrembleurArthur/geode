package com.geode.loaders;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.geode.annotations.Attribute;
import com.geode.builders.Builder;
import com.geode.builders.BuildersMap;
import com.geode.builders.ServerInfosBuilder;
import com.geode.net.ServerInfos;

import org.yaml.snakeyaml.Yaml;

public class Loader
{
    private Builder<?> builder;
    private HashMap<String, Method> buildMethods;

    public Loader(Builder<?> builder)
    {
        this.builder = builder.reset();
        buildMethods = new HashMap<>();
        for(Method method : builder.getClass().getMethods())
        {
            Attribute attribute = method.getAnnotation(Attribute.class);
            if(attribute != null)
            {
                buildMethods.put(attribute.value(), method);
            }
        }
    }

    public Builder<?> load(Map<String, Object> data) throws Exception
    {
        for(String key : data.keySet())
        {
            Method method = buildMethods.get(key);
            if(method != null)
            {
                Class<?> paramType = method.getParameterTypes()[0];
                Class<Builder<?>> builderClass = paramType.isPrimitive() ? null : BuildersMap.getBuilder(paramType);
                if(builderClass != null)
                {
                    Builder<?> subBuilder = new Loader(builderClass.getConstructor().newInstance())
                    .load((Map<String, Object>) data.get(key));
                    method.invoke(builder, subBuilder.build());
                }
                else
                {
                    method.invoke(builder, data.get(key));
                }
            }
        }
        return builder;
    }
}
