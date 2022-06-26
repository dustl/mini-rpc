package com.mini.rpc.rpcserver.handler;

import com.mini.rpc.core.code.RpcDecode;
import com.mini.rpc.core.code.RpcEncode;
import com.mini.rpc.rpcserver.handler.RpcRequestHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author:liwy
 * @date: 22.6.25
 * @Version:1.0
 */
public class ServerChannelHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                // 协议编码
                .addLast(new RpcEncode<>())
                // 协议解码
                .addLast(new RpcDecode())
                // 请求处理器
                .addLast(new RpcRequestHandler());

    }
}
