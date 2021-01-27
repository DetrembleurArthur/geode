package com.geode.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Control
{
    enum Type
    {
        SERVER_CLIENT,
        CLIENT_CLIENTS,
        CLIENT_CLIENT
    }
    String value() default "";
    Type type() default Type.SERVER_CLIENT;
}
