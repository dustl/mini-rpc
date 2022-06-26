package com.mini.rpc.provider.service;

import com.mini.rpc.providerapi.service.HelloService;
import com.mini.rpc.rpcserver.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author:liwy
 * @date: 22.5.26
 * @Version:1.0
 */
@RpcService(serviceInterfaceType = HelloService.class, version = "1.3")
@Slf4j
public class HelloAndDoServiceImpl implements HelloService {
    @Override
    public String say(String word) {
        try {
            log.info("{} do some thing...",word);
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "do some thing ..." + word;
    }
}
