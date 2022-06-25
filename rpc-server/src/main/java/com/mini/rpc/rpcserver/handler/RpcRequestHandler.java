package com.mini.rpc.rpcserver.handler;

import com.mini.rpc.core.common.RpcRequest;
import com.mini.rpc.core.common.RpcResponse;
import com.mini.rpc.core.protocol.MessageHeader;
import com.mini.rpc.core.protocol.MessageProtocol;
import com.mini.rpc.core.protocol.MsgStatus;
import com.mini.rpc.core.protocol.MsgType;
import com.mini.rpc.rpcserver.store.LocalServiceCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author:liwy
 * @date: 22.6.24
 * @Version:1.0
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcRequest>> {

    /**
     * 自定义线程池处理请求：
     */
    public static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10, 10, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10000)
    );

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<RpcRequest> rpcRequestMessageProtocol) throws Exception {
        /**多线程处理请求*/
        executor.submit(()->{
            log.info("处理请求 ：{}",rpcRequestMessageProtocol);
            MessageProtocol<RpcResponse> resMessageProtocol = new MessageProtocol<>();
            RpcResponse rpcResponse = new RpcResponse();
            MessageHeader messageHeader = rpcRequestMessageProtocol.getMessageHeader();
            messageHeader.setMsgType(MsgType.RESPONSE.getType());
            messageHeader.setStatus(MsgStatus.SUCCESS.getCode());
            try {
                Object result = handleRequest(rpcRequestMessageProtocol.getBody());
                rpcResponse.setData(result);
                resMessageProtocol.setMessageHeader(messageHeader);
                resMessageProtocol.setBody(rpcResponse);
            } catch (Throwable throwable) {
                messageHeader.setStatus(MsgStatus.FAIL.getCode());
                rpcResponse.setMessage(throwable.getMessage());
                log.error("调用异常 : ",throwable);
            }
            // 把结果数据写回去
            channelHandlerContext.writeAndFlush(resMessageProtocol);

        });
    }


    private Object handleRequest(RpcRequest rpcRequest) {
        String serviceName = rpcRequest.getServiceName();
        Object bean = LocalServiceCache.get(serviceName);
        try {
            if (bean == null) {
                throw new NullPointerException("service 不存在 " + serviceName);
            }
            Method method = bean.getClass().getMethod(rpcRequest.getMethod(), rpcRequest.getParameterType());
            return method.invoke(bean, rpcRequest.getParameters());
        } catch (Throwable throwable) {
            log.error("rpcService调用{}失败",serviceName);
            throw new RuntimeException(throwable);
        }
    }
}
