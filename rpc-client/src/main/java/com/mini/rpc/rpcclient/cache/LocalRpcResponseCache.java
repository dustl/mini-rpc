package com.mini.rpc.rpcclient.cache;

import com.mini.rpc.core.common.RpcResponse;
import com.mini.rpc.core.protocol.MessageProtocol;
import com.mini.rpc.rpcclient.transport.RpcFuture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求与响应映射
 * @Author:liwy
 * @date: 22.6.25
 * @Version:1.0
 */
public class LocalRpcResponseCache {
    /**
     * 存储已发送的请求，但是还没有响应
     * */
    private static final Map<String, RpcFuture<MessageProtocol<RpcResponse>>> requestCacheMap = new ConcurrentHashMap<>(1024);

    /**
     * 缓存请求
     * */
    public static void add(String requestId, RpcFuture<MessageProtocol<RpcResponse>> rpcFuture) {
        requestCacheMap.put(requestId, rpcFuture);
    }

    public static void removeIfResponse(String requestId, MessageProtocol<RpcResponse> messageProtocol) {
        // 获取缓存中的请求
        RpcFuture<MessageProtocol<RpcResponse>> rpcFuture = requestCacheMap.get(requestId);
        // 设置响应结果
        rpcFuture.setResponse(messageProtocol);

        //已获得响应的请求移除
        requestCacheMap.remove(requestId);

    }







}
