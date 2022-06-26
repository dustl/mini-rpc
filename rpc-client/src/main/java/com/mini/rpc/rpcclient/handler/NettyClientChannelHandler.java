package com.mini.rpc.rpcclient.handler;

import com.mini.rpc.core.code.RpcDecode;
import com.mini.rpc.core.code.RpcEncode;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author:liwy
 * @date: 22.5.25
 * @Version:1.0
 */
public class NettyClientChannelHandler extends ChannelInitializer<SocketChannel> {

    private RpcResponseHandler rpcResponseHandler;

    public NettyClientChannelHandler(RpcResponseHandler rpcResponseHandler) {
        this.rpcResponseHandler = rpcResponseHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                // 解码操作，入站
                .addLast(new RpcDecode())
                //解码，出站
                .addLast(new RpcEncode())
                // 处理请求响应
                .addLast(rpcResponseHandler);




    }
}
