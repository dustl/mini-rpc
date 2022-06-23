package com.mini.rpc.core.discovery;

import com.mini.rpc.core.common.ServiceInfo;

/**
 * @Author:liwy
 * @date: 22.5.10
 * @Version:1.0
 */
public interface DiscoveryService {

    ServiceInfo discoveryService(String serviceName) throws Exception;



}
