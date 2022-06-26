package com.mini.rpc.rpcclient.processor;

import com.mini.rpc.core.discovery.DiscoveryService;
import com.mini.rpc.rpcclient.annotation.RpcAutowired;
import com.mini.rpc.rpcclient.config.RpcClientProperties;
import com.mini.rpc.rpcclient.proxy.ClientStubProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @Author:liwy
 * @date: 22.5.24
 * @Version:1.0
 *bean后置处理器，获取所有的bean,并判断是否被{@link RpcAutowired}修饰（为远程服务接口），，并设置为代理对象
 *
 */
public class RpcClientProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private DiscoveryService discoveryService;

    private ClientStubProxyFactory clientStubProxyFactory;

    private RpcClientProperties rpcClientProperties;

    public RpcClientProcessor(DiscoveryService discoveryService, ClientStubProxyFactory clientStubProxyFactory, RpcClientProperties rpcClientProperties) {
        this.discoveryService = discoveryService;
        this.clientStubProxyFactory = clientStubProxyFactory;
        this.rpcClientProperties = rpcClientProperties;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        for (String beanName : configurableListableBeanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(beanName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null) {
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.getClass().getClassLoader());
                ReflectionUtils.doWithFields(clazz,field -> {
                    RpcAutowired rpcAutowired = AnnotationUtils.getAnnotation(field, RpcAutowired.class);
                    if (rpcAutowired != null) {
                        Object bean = applicationContext.getBean(clazz);
                        field.setAccessible(true);
                        // 修改服务暴露的接口为代理对象
                        ReflectionUtils.setField(field,bean,clientStubProxyFactory.getProxy(field.getType(),rpcAutowired.version(),discoveryService,rpcClientProperties));
                    }

                });
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
