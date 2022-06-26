package com.mini.rpc.rpcclient.transport;

/**
 * @Author:liwy
 * @date: 22.5.25
 * @Version:1.0
 */
public class NettyClientTransportFactory {


    public static NettyClientTransport getNettyClientTransport() {
        return new NettyClientTransportImpl();
    }
}
