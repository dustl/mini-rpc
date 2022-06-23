package com.mini.rpc.core.balance;

import com.mini.rpc.core.common.ServiceInfo;

import java.util.List;

/**
 * @Author:liwy
 * @date: 22.5.10
 * @Version:1.0
 */
public interface LoadBalance {

    ServiceInfo selectOneService(List<ServiceInfo> serviceInfoList);
}
