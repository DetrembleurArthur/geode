package com.geode.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * OnEvent is an annotation which defines some Event types to tag methods
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface OnEvent
{
    /**
     * Value event.
     *
     * @return the event
     */
    Event value() default Event.INIT;

    /**
     * The enum Event.
     */
    enum Event
    {
        /**
         * Init event.
         */
        INIT,
        /**
         * Reboot event.
         */
        REBOOT,
        /**
         * Init or reboot event.
         */
        INIT_OR_REBOOT,
        /**
         * Down event.
         */
        DOWN,
        /**
         * Query in event.
         */
        QUERY_IN
    }
}
