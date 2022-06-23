package com.mini.rpc.core.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author:liwy
 * @date: 22.5.2
 * @Version:1.0
 */

@Data
public class RpcRequest implements Serializable {

    /**
     * 服务名 + 版本
     * */
    private String  serviceName;

    /**
     * 调用的方法
     * */
    private String method;

    /**
     * 参数类型
     * */
    private Class<?>[] parameterType;

    /**
     * 参数
     * */
    private Object[] parameters;

}
