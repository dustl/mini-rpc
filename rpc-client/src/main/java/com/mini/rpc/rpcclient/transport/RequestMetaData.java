package com.mini.rpc.rpcclient.transport;

import com.mini.rpc.core.common.RpcRequest;
import com.mini.rpc.core.protocol.MessageProtocol;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author:liwy
 * @date: 22.5.25
 * @Version:1.0
 */
@Data
@Builder
public class RequestMetaData implements Serializable {

    /**
     * 协议
     * */
    private MessageProtocol<RpcRequest> messageProtocol;

    /**
     * 请求的服务地址
     * */
    private String address;

    /**
     * 请求的服务端口
     * */
    private Integer port;

    /**
     * 超时参数
     * */
    private Integer timeOut;




}
