package com.mini.rpc.core.register;

import com.mini.rpc.core.common.ServiceInfo;
import com.mini.rpc.core.common.ZkConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;

/**
 * @Author:liwy
 * @date: 22.6.23
 * @Version:1.0
 */
@Slf4j
public class ZookeeperRegistryServiceImpl implements RegistryService{


    private ServiceDiscovery<ServiceInfo> serviceDiscovery;

    /**
    * @param address 服务中心地址
    * */
    public ZookeeperRegistryServiceImpl(String address) {
        CuratorFramework client = CuratorFrameworkFactory.newClient(address, new ExponentialBackoffRetry(ZkConstant.BASE_SLEEP, ZkConstant.MAX_RETRY));
        client.start();
        JsonInstanceSerializer<ServiceInfo> jsonInstanceSerializer = new JsonInstanceSerializer<>(ServiceInfo.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                .client(client)
                .serializer(jsonInstanceSerializer)
                .basePath(ZkConstant.ZK_BASE_PATH)
                .build();
        try {
            this.serviceDiscovery.start();
        } catch (Exception e) {
            log.error("error in serviceDiscovery : " ,e);
        }

    }

    @Override
    public void register(ServiceInfo serviceInfo) throws Exception {
        ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance.<ServiceInfo>builder()
                .name(serviceInfo.getServiceName())
                .address(serviceInfo.getAddress())
                .port(serviceInfo.getPort())
                .payload(serviceInfo)
                .build();

        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegister(ServiceInfo serviceInfo) throws Exception {
        ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance.<ServiceInfo>builder()
                .name(serviceInfo.getServiceName())
                .address(serviceInfo.getAddress())
                .port(serviceInfo.getPort())
                .payload(serviceInfo)
                .build();

    }

    @Override
    public void destroy() throws IOException {
          serviceDiscovery.close();
    }
}
