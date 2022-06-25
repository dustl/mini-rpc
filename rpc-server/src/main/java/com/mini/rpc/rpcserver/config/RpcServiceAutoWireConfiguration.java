package com.mini.rpc.rpcserver.config;

import com.mini.rpc.core.register.RegistryService;
import com.mini.rpc.core.register.ZookeeperRegistryServiceImpl;
import com.mini.rpc.rpcserver.RpcServiceProvider;
import com.mini.rpc.rpcserver.annotation.RpcService;
import com.mini.rpc.rpcserver.transport.NettyRpcServer;
import com.mini.rpc.rpcserver.transport.RpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:liwy
 * @date: 22.6.25
 * @Version:1.0
 */
@Configuration
@EnableConfigurationProperties(RpcServiceProperties.class)
public class RpcServiceAutoWireConfiguration {

    @Autowired
    private RpcServiceProperties rpcServiceProperties;


    /**
     * 注入注册中心
     * */
    @Bean
    @ConditionalOnMissingBean(RegistryService.class)
    public RegistryService registryService() {
        return  new ZookeeperRegistryServiceImpl(rpcServiceProperties.getRegistryAddress());
    }

    /**
     * 注入netty服务
     * */
    @Bean
    @ConditionalOnMissingBean(RpcServer.class)
    public RpcServer rpcServer() {
        return new NettyRpcServer();
    }


    /**
     * 注入服务提供方
     */
    @Bean
    @ConditionalOnMissingBean(RpcServiceProvider.class)
    public RpcServiceProvider rpcServiceProvider(RegistryService registryService,
                                                 RpcServer rpcServer,
                                                 RpcServiceProperties rpcServiceProperties) {
        return new RpcServiceProvider(registryService, rpcServer, rpcServiceProperties);
    }



}
