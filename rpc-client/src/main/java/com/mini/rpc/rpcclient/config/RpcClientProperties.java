package com.mini.rpc.rpcclient.config;

import com.mini.rpc.core.balance.LoadBalance;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author:liwy
 * @date: 22.6.25
 * @Version:1.0
 */
@Data
public class RpcClientProperties {

    /**
     * 负载均衡
     * */
    private String balance;

    /**
     *序列化
     * */
    private String serialization;

    /**
     * 服务发现地址
     * */
    private String discoveryAddress = "127.0.0.1:2181";

    /**
     * 服务超时参数
     * */
    private Integer timeout;



}
