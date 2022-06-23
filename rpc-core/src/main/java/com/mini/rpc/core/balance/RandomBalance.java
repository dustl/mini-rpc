package com.mini.rpc.core.balance;

import com.mini.rpc.core.common.ServiceInfo;

import java.util.List;
import java.util.Random;

/**
 * @Author:liwy
 * @date: 22.6.23
 * @Version:1.0
 */
public class RandomBalance implements LoadBalance {

    private Random random = new Random();

    @Override
    public ServiceInfo selectOneService(List<ServiceInfo> serviceInfoList) {
        return serviceInfoList.get(random.nextInt(serviceInfoList.size()));
    }


}
