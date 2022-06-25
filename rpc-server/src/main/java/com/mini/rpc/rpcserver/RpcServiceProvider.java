package com.mini.rpc.rpcserver;

import com.mini.rpc.core.common.ServiceInfo;
import com.mini.rpc.core.common.ServiceUtil;
import com.mini.rpc.core.register.RegistryService;
import com.mini.rpc.rpcserver.annotation.RpcService;
import com.mini.rpc.rpcserver.config.RpcServiceProperties;
import com.mini.rpc.rpcserver.store.LocalServiceCache;
import com.mini.rpc.rpcserver.transport.RpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @Author:liwy
 * @date: 22.6.24
 * @Version:1.0
 */
@Slf4j
public class RpcServiceProvider implements BeanPostProcessor, CommandLineRunner {

    private RegistryService registryService;

    private RpcServer rpcServer;

    private RpcServiceProperties rpcServiceProperties;


    public RpcServiceProvider(RegistryService registryService, RpcServer rpcServer, RpcServiceProperties rpcServiceProperties) {
        this.registryService = registryService;
        this.rpcServer = rpcServer;
        this.rpcServiceProperties = rpcServiceProperties;
    }


    /**
     * 所有Bean实例化后，把暴露出的服务，注册到注册中心，
     * 并开启netty服务处理请求
     * */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        if (rpcService != null) {
            try {
                String serviceName = rpcService.serviceInterfaceType().getName();
                String version = rpcService.version();
                // 把这个提供服务bean缓存下来,如果一个服务接口多个实现，可以用版本号区分
                LocalServiceCache.storeService(ServiceUtil.getServiceKey(serviceName, version), bean);
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setAppName(rpcServiceProperties.getAppName());
                serviceInfo.setServiceName(ServiceUtil.getServiceKey(serviceName, version));
                serviceInfo.setVersion(version);
                serviceInfo.setAddress(InetAddress.getLocalHost().getHostAddress());
                serviceInfo.setPort(rpcServiceProperties.getPort());
                // 服务注册
                registryService.register(serviceInfo);
            } catch (Exception e) {
                log.error("后置处理服务bean异常 : ", e);
            }
        }

        return bean;
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(()->{rpcServer.start(rpcServiceProperties.getPort());}).start();
        log.info("rpc server :{} ,port: {} appName:{}", rpcServer, rpcServiceProperties.getPort(), rpcServiceProperties.getAppName());
        // 服务关闭后，钩子方法
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                //把服务从zk上清除
                registryService.destroy();
            } catch (IOException e) {
                log.error("服务关闭失败",e);
            }
        }));
    }


}
