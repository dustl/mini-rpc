package com.mini.rpc.provider.service;

import com.mini.rpc.providerapi.service.HelloService;
import com.mini.rpc.rpcserver.annotation.RpcService;

/**
 * @Author:liwy
 * @date: 22.6.26
 * @Version:1.0
 */
@RpcService(serviceInterfaceType = HelloService.class,version = "1.2")
public class HelloServiceImpl2 implements HelloService {
    @Override
    public String say(String word) {
        return "rpc say2: " +word;
    }
}
