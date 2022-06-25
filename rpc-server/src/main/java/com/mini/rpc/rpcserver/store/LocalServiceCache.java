package com.mini.rpc.rpcserver.store;

import java.util.HashMap;
import java.util.Map;

/**
 * 将暴露的服务缓存到本地
 *
 * @Author:liwy
 * @date: 22.6.24
 * @Version:1.0
 */
public class LocalServiceCache {

    public static final Map<String, Object> serviceMap = new HashMap<>(1024);

    public static void storeService(String serviceName, Object server) {
        //  merge : 如果通过 serviceName找不到，那么就put 这个, key : serviceName  value :server
        // 如果通过serviceName 找到，那么就通过自己的 BiFunction 实现，这里返回 objectNew，也就是server
        serviceMap.merge(serviceName, server, (objectOld, objectNew) -> objectNew);
    }

    public static Object get(String serviceName) {
        return serviceMap.get(serviceName);
    }

}
