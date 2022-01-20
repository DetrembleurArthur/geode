package com.geode.net.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Inject is an annotation which tag class fields who are used to inject objects by the ProtocolHandler
 *
 * @author DetrembleurArthur
 * @version 1.0.0
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Inject
{

}
