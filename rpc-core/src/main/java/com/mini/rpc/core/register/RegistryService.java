package com.mini.rpc.core.register;

import com.mini.rpc.core.common.ServiceInfo;

import java.io.IOException;
/**
 * @Author:liwy
 * @date: 22.5.3
 * @Version:1.0
 */
public interface RegistryService {

    void register(ServiceInfo serviceInfo) throws Exception;

    void unRegister(ServiceInfo serviceInfo) throws Exception;

    void destroy() throws IOException;

}
