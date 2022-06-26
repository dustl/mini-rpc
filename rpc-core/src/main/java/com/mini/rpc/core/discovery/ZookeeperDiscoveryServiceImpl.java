package com.mini.rpc.core.discovery;

import com.mini.rpc.core.balance.LoadBalance;
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
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @Author:liwy
 * @date: 22.6.23
 * @Version:1.0
 */
@Slf4j
public class ZookeeperDiscoveryServiceImpl implements DiscoveryService {

    private ServiceDiscovery<ServiceInfo> serviceDiscovery;

    private LoadBalance loadBalance;
    /**
     * @param address 注册中心地址
     * @param loadBalance 负载均衡策略
     * */
    public ZookeeperDiscoveryServiceImpl(String address,LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
        CuratorFramework client = CuratorFrameworkFactory.newClient(address, new ExponentialBackoffRetry(ZkConstant.BASE_SLEEP, ZkConstant.MAX_RETRY));
        client.start();
        JsonInstanceSerializer<ServiceInfo>jsonInstanceSerializer = new JsonInstanceSerializer<>(ServiceInfo.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.<ServiceInfo>builder(ServiceInfo.class)
                .client(client)
                .serializer(jsonInstanceSerializer)
                .basePath(ZkConstant.ZK_BASE_PATH)
                .build();
        try {
            serviceDiscovery.start();
        } catch (Exception e) {
            log.error("start serviceDiscovery appear error : {}",e);
        }
    }

    @Override
    public ServiceInfo discoveryService(String serviceName) throws Exception {
        Collection<ServiceInstance<ServiceInfo>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        return CollectionUtils.isEmpty(serviceInstances) ? null :
                loadBalance.selectOneService
                        (serviceInstances.stream().map(ServiceInstance::getPayload).collect(Collectors.toList()));

    }
}
