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
        CLASSIC, //client->server | server->client
        TOPIC, //client->clients by topic
        DIRECT, //client->client | client->clients by id
        QUEUE //client->client?
    }
    String value() default "";
    Type type() default Type.CLASSIC;
}
