package com.mini.rpc.core.code;

import com.mini.rpc.core.common.RpcRequest;
import com.mini.rpc.core.common.RpcResponse;
import com.mini.rpc.core.protocol.MessageHeader;
import com.mini.rpc.core.protocol.MessageProtocol;
import com.mini.rpc.core.protocol.MsgType;
import com.mini.rpc.core.protocol.ProtocolConstants;
import com.mini.rpc.core.serialization.RpcSerialization;
import com.mini.rpc.core.serialization.SerializationStrategyFactory;
import com.mini.rpc.core.serialization.SerializationTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 消息解码，解析发送方过来的数据
 * @Author:liwy
 * @date: 22.6.23
 * @Version:1.0
 */
@Slf4j
public class RpcDecode extends ByteToMessageDecoder {

    /**
     *
     * ------------------------------------------------------------------
     * 魔数 2 byte | 协议版本号 1 byte  |序列化算法 1 byte |  报文类型 1byte|
     * ------------------------------------------------------------------
     * 状态 1 byte | 消息id 32 byte    | 数据长度4 byte                  |
     * ------------------------------------------------------------------
     *               消息内容
     *------------------------------------------------------------------
     *
     * */

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < ProtocolConstants.HEADER_TOTAL_LEN) {
            // 消息头的总大小 为 42 byte ，只要是小于它就丢弃
            log.error("丢弃消息");
            return;
        }
        log.info("开始解码:{}",byteBuf);
        log.info("current read index : {}" ,byteBuf.readerIndex());
        // 标记读指针位置
        byteBuf.markReaderIndex();
        // 魔数
        short magic = byteBuf.readShort();
        log.info("magic : {}", magic);
        byte version = byteBuf.readByte();
        byte serialization = byteBuf.readByte();
        byte msgType = byteBuf.readByte();
        byte status = byteBuf.readByte();
        CharSequence charSequence = byteBuf.readCharSequence(ProtocolConstants.REQ_LEN, Charset.forName("UTF-8"));
        String messageId = String.valueOf(charSequence);
        int dataLen = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataLen) {
            log.error("可读的数据长度小于消息头所设置的可读长度");
            return;
        }
        byte[] data = new byte[dataLen];
        byteBuf.readBytes(data);
        MsgType type = MsgType.findByType(msgType);
        if (type == null) {
            log.error("无法找到该报文类型");
            return;
        }
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMagic(magic);
        messageHeader.setVersion(version);
        messageHeader.setSerialization(serialization);
        messageHeader.setMsgType(type.getType());
        messageHeader.setStatus(status);
        messageHeader.setMessageId(messageId);
        messageHeader.setMsgLen(dataLen);
        // 判断报文类型，以此来判断是请求，还是响应
        RpcSerialization rpcSerialization = SerializationStrategyFactory.getSerialization(SerializationTypeEnum.parseByType(serialization));

        switch (type) {
            case REQUEST:
                // 反序列数据
                RpcRequest rpcRequest = rpcSerialization.deSerialize(data, RpcRequest.class);
                if (rpcRequest != null) {
                    MessageProtocol<RpcRequest> messageProtocol = new MessageProtocol<>();
                    messageProtocol.setMessageHeader(messageHeader);
                    messageProtocol.setBody(rpcRequest);
                    list.add(messageProtocol);
                }else {
                    log.error("反序列化后的rpcRequest为空");
                }
                break;
            case RESPONSE:
                RpcResponse rpcResponse = rpcSerialization.deSerialize(data, RpcResponse.class);
                if (rpcResponse != null) {
                    MessageProtocol<RpcResponse> messageProtocol = new MessageProtocol<>();
                    messageProtocol.setMessageHeader(messageHeader);
                    messageProtocol.setBody(rpcResponse);
                    list.add(messageProtocol);

                }else {
                    log.error("反序列化后的rpcResponse为空");
                }
                break;
            default:
                log.error("不支持该报文类型");
        }

    }
}
