package com.mini.rpc.rpcclient.config;

import com.mini.rpc.core.balance.FullRoundBalance;
import com.mini.rpc.core.balance.LoadBalance;
import com.mini.rpc.core.balance.RandomBalance;
import com.mini.rpc.core.discovery.DiscoveryService;
import com.mini.rpc.core.discovery.ZookeeperDiscoveryServiceImpl;
import com.mini.rpc.rpcclient.processor.RpcClientProcessor;
import com.mini.rpc.rpcclient.proxy.ClientStubProxyFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

/**
 * @Author:liwy
 * @date: 22.6.25
 * @Version:1.0
 */
@Configuration
public class RpcClientAutoConfiguration {


    @Bean
    public RpcClientProperties rpcClientProperties(Environment environment) {
        BindResult<RpcClientProperties> bindResult = Binder.get(environment).bind("rpc.client", RpcClientProperties.class);
        return bindResult.get();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientStubProxyFactory clientStubProxyFactory() {
        return new ClientStubProxyFactory();
    }


    @Bean(name = "loadBalance")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client",name = "balance",havingValue = "randomBalance")
    public LoadBalance randomBalance() {
        return new RandomBalance();
    }

    /**
     * 默认轮询策略
     * */
    @Primary
    @Bean(name = "loadBalance")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client",name = "balance",havingValue = "fullRoundBalance",matchIfMissing = true)
    public LoadBalance fullRoundBalance() {
        return new FullRoundBalance();

    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({RpcClientProperties.class,LoadBalance.class})
    public DiscoveryService discoveryService(RpcClientProperties rpcClientProperties,LoadBalance loadBalance) {
        return new ZookeeperDiscoveryServiceImpl(rpcClientProperties.getDiscoveryAddress(), loadBalance);
    }


    @Bean
    @ConditionalOnMissingBean
    public RpcClientProcessor rpcClientProcessor(ClientStubProxyFactory clientStubProxyFactory,
                                                 RpcClientProperties rpcClientProperties,
                                                 DiscoveryService discoveryService) {
        return new RpcClientProcessor(discoveryService, clientStubProxyFactory, rpcClientProperties);
    }



}
