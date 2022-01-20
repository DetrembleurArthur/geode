package com.geode.net.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Control is an annotation which tag methods who are used to intercept Query parameters of a certain type
 *
 * @author DetrembleurArthur
 * @version 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Control
{
    /**
     * The type of the Query object catch
     *
     * @return the string
     */
    String value() default "";

    String state() default "DEFAULT";

    /**
     * The type of the Control
     *
     * @return the type
     */
    Type type() default Type.CLASSIC;

    /**
     * The enum Type defines the type of a Control.
     */
    enum Type
    {
        /**
         * Classic type.
         */
        CLASSIC, //client->server | server->client
        /**
         * Topic type.
         */
        TOPIC, //client->clients by topic
        /**
         * Direct type.
         */
        DIRECT, //client->client | client->clients by id
        /**
         * Queue type.
         */
        QUEUE //client->client?
    }
}
