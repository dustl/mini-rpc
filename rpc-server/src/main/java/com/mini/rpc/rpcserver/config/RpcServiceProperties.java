package com.mini.rpc.rpcserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author:liwy
 * @date: 22.6.25
 * @Version:1.0
 */
@Data
@ConfigurationProperties(prefix = "rpc.server")
public class RpcServiceProperties {

    /**
     * 服务默认端口
     * */
    private int port = 8089;

    /**
     * 服务名称
     * */
    private String appName;

    /**
     * 注册中心地址
     * */
    private String registryAddress = "127.0.0.1:2181";

}
