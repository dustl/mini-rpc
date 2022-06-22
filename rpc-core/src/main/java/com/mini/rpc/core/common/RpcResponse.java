package com.mini.rpc.core.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author:liwy
 * @date: 22.6.22
 * @Version:1.0
 */
@Data
public class RpcResponse implements Serializable {

    private Object data;

    private String message;




}
