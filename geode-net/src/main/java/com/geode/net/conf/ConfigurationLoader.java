package com.geode.net.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract class ConfigurationLoader
{
    private static final HashMap<String, Class<?>> loadersMap = new HashMap<>();

    static
    {
        try
        {
            Class.forName(YamlConfigurationLoader.class.getName());
            Class.forName(JsonConfigurationLoader.class.getName());
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private final File confFile;

    public static ConfigurationLoader get(String confFilename) throws Exception
    {
        int i = confFilename.lastIndexOf(".");
        if (i != -1 && confFilename.length() >= i + 1)
        {
            String ext = confFilename.substring(i + 1);
            Class<?> loaderClass = loadersMap.get(ext);
            if (loaderClass != null)
            {
                return (ConfigurationLoader) loaderClass.getConstructor(File.class).newInstance(new File(confFilename));
            }
        }
        return null;
    }

    public ConfigurationLoader(File confFile)
    {
        this.confFile = confFile;
    }

    public abstract Map<String, Object> parse() throws FileNotFoundException;

    public final <T> T load(Class<T> beanClass) throws FileNotFoundException
    {
        Map<String, Object> map = parse();
        try
        {
            return loadBeanFromNode(map, beanClass);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    protected <T> T loadBeanFromNode(Map<String, Object> node, Class<T> beanClass) throws Exception
    {
        final T bean = beanClass.getConstructor().newInstance();
        node.forEach((s, o) -> {
            Arrays.stream(beanClass.getMethods())
                .filter(method -> method.isAnnotationPresent(Attribute.class))
                .filter(method -> method.getAnnotation(Attribute.class).value().equals(s))
                    .forEach(method -> {
                        try
                        {
                            Class<?> paramType = method.getParameterTypes()[0];
                            Object data = loadBeanFromData(o, paramType, method.getAnnotation(Attribute.class));
                            System.out.println("invoke: " + method.getName() + " " + data);
                            method.invoke(bean, data);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    });
        });
        return bean;
    }

    protected Object loadBeanFromData(Object data, Class<?> dataType, Attribute attribute) throws Exception
    {
        if(dataType.isAnnotationPresent(AttributeHolder.class))
        {
            return loadBeanFromNode((Map<String, Object>) data, dataType);
        }
        else if(ArrayList.class.isAssignableFrom(dataType))
        {
            final ArrayList<Object> list = (ArrayList<Object>) dataType.getConstructor().newInstance();
            ((ArrayList<?>)data).forEach(o -> {
                try
                {
                    list.add(loadBeanFromData(o, attribute.innerElement(), null));
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
            return list;
        }
        else if(Map.class.isAssignableFrom(dataType))
        {
            Map<String, Object> map = new HashMap<>();
            ((Map<?, ?>)data).forEach((s, o) -> {
                try
                {
                    map.put((String) s, loadBeanFromData(o, attribute.innerElement(), null));
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
            return map;
        }
        else
        {
            return data;
        }
    }

    public File getConfFile()
    {
        return confFile;
    }

    public static <T extends ConfigurationLoader> void register(Class<T> loaderClass)
    {
        Loader loader = loaderClass.getAnnotation(Loader.class);
        if (loader != null)
        {
            loadersMap.put(loader.value(), loaderClass);
        }
    }
}
