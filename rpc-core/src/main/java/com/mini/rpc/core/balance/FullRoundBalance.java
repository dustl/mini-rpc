package com.mini.rpc.core.balance;

import com.mini.rpc.core.common.ServiceInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认轮询策略
 * @Author:liwy
 * @date: 22.5.5
 * @Version:1.0
 */
@Slf4j
public class FullRoundBalance implements LoadBalance {

    private int index = 0;

    /**
     * 并发调用
     */
    @Override
    public synchronized ServiceInfo selectOneService(List<ServiceInfo> serviceInfoList) {
        // 防止越界，重新计算
        if (index >= serviceInfoList.size()) {
            index = 0;
        }
        ServiceInfo serviceInfo = serviceInfoList.get(index++);
        return serviceInfo;
    }
}
