package com.geode.engine.binding;


import java.util.HashMap;

public class FieldPropertiesScheme<T>
{
    private final Class<T> class_;
    private final HashMap<String, FieldPropertyScheme<?>> fieldPropertySchemes = new HashMap<>();

    public FieldPropertiesScheme(Class<T> class_)
    {
        this.class_ = class_;
    }

    public <U> FieldPropertyScheme<U> get(String fieldName)
    {
        if(fieldPropertySchemes.containsKey(fieldName))
            return (FieldPropertyScheme<U>) fieldPropertySchemes.get(fieldName);
        var scheme = new FieldPropertyScheme<U>(class_, fieldName);
        fieldPropertySchemes.put(fieldName, scheme);
        return scheme;
    }
}
