package com.mini.rpc.rpcclient.proxy;

import com.mini.rpc.core.common.RpcRequest;
import com.mini.rpc.core.common.RpcResponse;
import com.mini.rpc.core.common.ServiceInfo;
import com.mini.rpc.core.common.ServiceUtil;
import com.mini.rpc.core.discovery.DiscoveryService;
import com.mini.rpc.core.exception.ResourceNotFoundException;
import com.mini.rpc.core.exception.RpcException;
import com.mini.rpc.core.protocol.MessageHeader;
import com.mini.rpc.core.protocol.MessageProtocol;
import com.mini.rpc.core.protocol.MsgStatus;
import com.mini.rpc.rpcclient.config.RpcClientProperties;
import com.mini.rpc.rpcclient.transport.NettyClientTransport;
import com.mini.rpc.rpcclient.transport.NettyClientTransportFactory;
import com.mini.rpc.rpcclient.transport.RequestMetaData;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理服务
 *
 * @Author:liwy
 * @date: 22.5.25
 * @Version:1.0
 */
@Slf4j
public class ClientStubInvocationHandler implements InvocationHandler {

    private DiscoveryService discoveryService;

    private RpcClientProperties rpcClientProperties;

    private Class<?> clazz;

    private String version;

    public ClientStubInvocationHandler(DiscoveryService discoveryService, RpcClientProperties rpcClientProperties, Class<?> clazz, String version) {
        this.discoveryService = discoveryService;
        this.rpcClientProperties = rpcClientProperties;
        this.clazz = clazz;
        this.version = version;
    }

    /**
     * 代理发送请求，封装请求数据
     *
     * */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ServiceInfo serviceInfo = discoveryService.discoveryService(ServiceUtil.getServiceKey(clazz.getName(), version));
        if (serviceInfo == null) {
            log.info("无法找到该服务:{}", clazz.getName());
            throw new ResourceNotFoundException("404,not find this service :" + clazz.getName());
        }
        MessageProtocol<RpcRequest> messageProtocol = new MessageProtocol<>();
        messageProtocol.setMessageHeader(MessageHeader.bulid(rpcClientProperties.getSerialization()));
        RpcRequest rpcRequest = new RpcRequest();
        // 设置请求的服务类名与版本
        rpcRequest.setServiceName(ServiceUtil.getServiceKey(clazz.getName(), version));
        // 设置请求服务的方法
        rpcRequest.setMethod(method.getName());
        // 设置请求服务的参数类型
        rpcRequest.setParameterType(method.getParameterTypes());
        // 设置请求服务的实际参数
        rpcRequest.setParameters(args);
        messageProtocol.setBody(rpcRequest);

        log.info("开始发送请求....");
        // 发送网络请求
        NettyClientTransport nettyClientTransport = NettyClientTransportFactory.getNettyClientTransport();
        // 构造发送体
        RequestMetaData requestMetaData = RequestMetaData.builder()
                .address(serviceInfo.getAddress())
                .port(serviceInfo.getPort())
                .timeOut(rpcClientProperties.getTimeout())
                .messageProtocol(messageProtocol).build();

        MessageProtocol<RpcResponse> responseMessageProtocol = nettyClientTransport.sendMessage(requestMetaData);
        if (responseMessageProtocol == null) {
            log.error("请求失败,rpc调用超时");
            throw new RpcException("rpc 调用超时,设置的超时配置:{}"+ rpcClientProperties.getTimeout());
        }
        // 状态不是成功
        if (!MsgStatus.isSuccess(responseMessageProtocol.getMessageHeader().getStatus())) {
            log.error("rpc请求失败,{}", responseMessageProtocol.getBody().getMessage());
            throw new RpcException(responseMessageProtocol.getBody().getMessage());
        }
        return responseMessageProtocol.getBody().getData();
    }
}
