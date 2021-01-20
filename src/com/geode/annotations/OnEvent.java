package com.geode.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface OnEvent
{
	public enum Event
	{
		INIT
	}
	
	Event value() default Event.INIT;
}
