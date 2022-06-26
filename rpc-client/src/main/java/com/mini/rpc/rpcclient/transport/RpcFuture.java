package com.mini.rpc.rpcclient.transport;

import com.mini.rpc.core.common.RpcResponse;

import java.util.concurrent.*;

/**
 * 结果做异步返回处理
 * @Author:liwy
 * @date: 22.5.25
 * @Version:1.0
 */

public class RpcFuture<T> implements Future<T> {

    private T response;

    /**
     * 请求和响应一一对应
     * */
    CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        if (response != null) {
            return true;
        }

        return false;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        // 直到返回结果后，1 ->0 ,才返回结果，否则等待
        countDownLatch.await();
        return response;
    }

    /**
     * 超时等待
     * */
    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (countDownLatch.await(timeout, unit)) {
            return response;
        }
        return null;
    }

    public void setResponse(T response) {
        this.response = response;
        // 一旦有返回结果后，countdown
        countDownLatch.countDown();
    }
}
