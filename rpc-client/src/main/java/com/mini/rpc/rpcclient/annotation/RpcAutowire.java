package com.mini.rpc.rpcclient.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Autowired
public @interface RpcAutowire {

    String version() default "1.0";


}
