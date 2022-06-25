package com.mini.rpc.rpcserver.transport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @Author:liwy
 * @date: 22.6.25
 * @Version:1.0
 */
@Slf4j
public class NettyRpcServer implements RpcServer {

    @Override
    public void start(int port) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            String serverHost = InetAddress.getLocalHost().getHostAddress();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelHandler())
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture channelFuture = serverBootstrap.bind(serverHost, port).sync();
            log.info("RpcServer address :{}  port : {} `");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("NettyRpc连接异常",e);
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
