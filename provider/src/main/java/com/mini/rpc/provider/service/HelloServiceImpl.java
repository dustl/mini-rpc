package com.mini.rpc.provider.service;

import com.mini.rpc.providerapi.service.HelloService;
import com.mini.rpc.rpcserver.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author:liwy
 * @date: 22.6.25
 * @Version:1.0
 */
@RpcService(serviceInterfaceType = HelloService.class, version = "1.0")
@Slf4j
public class HelloServiceImpl implements HelloService {
    @Override
    public String say(String word) {
        log.info("hello rpc , you say : {}", word);
        return "hello rpc,"+word;
    }
}
