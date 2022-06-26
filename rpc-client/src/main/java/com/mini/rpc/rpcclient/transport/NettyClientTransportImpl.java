package com.mini.rpc.rpcclient.transport;

import com.mini.rpc.core.common.RpcRequest;
import com.mini.rpc.core.common.RpcResponse;
import com.mini.rpc.core.protocol.MessageProtocol;
import com.mini.rpc.rpcclient.cache.LocalRpcResponseCache;
import com.mini.rpc.rpcclient.handler.NettyClientChannelHandler;
import com.mini.rpc.rpcclient.handler.RpcResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author:liwy
 * @date: 22.6.25
 * @Version:1.0
 */
@Slf4j
public class NettyClientTransportImpl implements NettyClientTransport {

    private Bootstrap bootstrap;

    private EventLoopGroup eventLoopGroup;

    private RpcResponseHandler rpcResponseHandler;


    public NettyClientTransportImpl() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        rpcResponseHandler = new RpcResponseHandler();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientChannelHandler(rpcResponseHandler));

    }

    @Override
    public MessageProtocol<RpcResponse> sendMessage(RequestMetaData requestMetaData) throws Exception {
        MessageProtocol<RpcRequest> messageProtocol = requestMetaData.getMessageProtocol();
        RpcFuture<MessageProtocol<RpcResponse>> rpcFuture = new RpcFuture<>();
        LocalRpcResponseCache.add(messageProtocol.getMessageHeader().getMessageId(), rpcFuture);
        // Tcp
        ChannelFuture channelFuture = bootstrap.connect(requestMetaData.getAddress(), requestMetaData.getPort()).sync();
        channelFuture.addListener((ChannelFutureListener) arg -> {
            if (channelFuture.isSuccess()) {
                log.info("connect rpc server {} on rpc port {} success", requestMetaData.getAddress(), requestMetaData.getPort());

            } else {
                log.error("connect rpc server {} on rpc port {} fail !", requestMetaData.getAddress(), requestMetaData.getPort());
                channelFuture.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        //连接成功后，发送数据
        channelFuture.channel().writeAndFlush(messageProtocol);
        log.info("请求 :{},开始等待...",messageProtocol);
        return requestMetaData.getTimeOut()!=null ?
                  rpcFuture.get(requestMetaData.getTimeOut(), TimeUnit.MILLISECONDS)
                : rpcFuture.get();
    }
}
