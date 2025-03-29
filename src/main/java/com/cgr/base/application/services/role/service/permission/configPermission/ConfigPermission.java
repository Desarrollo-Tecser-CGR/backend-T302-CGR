package com.cgr.base.application.services.role.service.permission.configPermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConfigPermission {
    String value();  // nombre del permiso
    String userIdParam() default "usuarioId";// El nombre del parámetro que contiene el userId

}
