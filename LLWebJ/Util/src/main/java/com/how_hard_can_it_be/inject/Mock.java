package com.how_hard_can_it_be.inject;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Retention( RetentionPolicy.RUNTIME )
@Target( {ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER} )
public @interface Mock {}