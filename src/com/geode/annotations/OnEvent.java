package com.geode.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface OnEvent
{
	enum Event
	{
		INIT,
		REBOOT,
		INIT_OR_REBOOT,
		DOWN,
		QUERY_IN
	}
	
	Event value() default Event.INIT;
}
