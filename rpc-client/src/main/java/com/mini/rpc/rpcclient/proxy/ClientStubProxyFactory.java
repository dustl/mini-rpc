package com.mini.rpc.rpcclient.proxy;

import com.mini.rpc.core.discovery.DiscoveryService;
import com.mini.rpc.rpcclient.config.RpcClientProperties;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:liwy
 * @date: 22.5.25
 * @Version:1.0
 */
public class ClientStubProxyFactory {

    private final Map<Class<?>, Object> objectMap = new HashMap<>();

    /**
     * @param clazz 服务接口
     * @param version 版本号
     * @param discoveryService 服务发现
     * @param rpcClientProperties rpc调用方配置
     * */
    public <T> T getProxy(Class<?> clazz, String version, DiscoveryService discoveryService, RpcClientProperties rpcClientProperties) {
        // clz即clzz
        return (T) objectMap.computeIfAbsent(clazz, clz ->
            Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new ClientStubInvocationHandler(discoveryService, rpcClientProperties,
                    clz, version))
        );

    }



}
