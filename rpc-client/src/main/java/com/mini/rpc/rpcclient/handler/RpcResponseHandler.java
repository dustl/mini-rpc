package com.mini.rpc.rpcclient.handler;

import com.mini.rpc.core.common.RpcResponse;
import com.mini.rpc.core.protocol.MessageProtocol;
import com.mini.rpc.rpcclient.cache.LocalRpcResponseCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;

/**
 * 数据响应处理器
 * @Author:liwy
 * @date: 22.5.15
 * @Version:1.0
 */
@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcResponse>> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<RpcResponse> messageProtocol) throws Exception {
        log.info("响应来了，{}",messageProtocol);
        String messageId = messageProtocol.getMessageHeader().getMessageId();
        // 设置响应数据
        LocalRpcResponseCache.removeIfResponse(messageId, messageProtocol);
    }
}
