package com.mini.rpc.rpcserver.annotation;


import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface RpcService {

    /**
     * 暴露服务的接口类型
     * */
    Class<?> serviceInterfaceType() default Object.class;

    /**
     * 服务版本号
     * */
    String version() default "1.0";
}
