package com.geode.net.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Protocol.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Protocol
{
    /**
     * Value string.
     *
     * @return the string
     */
    String value() default "default";

    /**
     * Scope scope.
     *
     * @return the scope
     */
    Scope scope() default Scope.CONNECTION;

    /**
     * The enum Scope.
     */
    enum Scope
    {
        /**
         * Connection scope.
         */
        CONNECTION,
        /**
         * Query scope.
         */
        QUERY
    }
}
