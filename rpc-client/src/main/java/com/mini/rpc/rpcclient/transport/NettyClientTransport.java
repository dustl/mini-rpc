package com.mini.rpc.rpcclient.transport;

import com.mini.rpc.core.common.RpcResponse;
import com.mini.rpc.core.protocol.MessageProtocol;

/**
 * @Author:liwy
 * @date: 22.5.25
 * @Version:1.0
 */
public interface NettyClientTransport {

    /**
     * 发送数据
     * @param requestMetaData 请求
     * @return 结果响应
     * */
    MessageProtocol<RpcResponse> sendMessage(RequestMetaData requestMetaData) throws Exception;
}
