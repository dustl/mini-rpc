package com.mini.rpc.core.code;

import com.mini.rpc.core.protocol.MessageHeader;
import com.mini.rpc.core.protocol.MessageProtocol;
import com.mini.rpc.core.serialization.RpcSerialization;
import com.mini.rpc.core.serialization.SerializationStrategyFactory;
import com.mini.rpc.core.serialization.SerializationTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 *
 * 消息编码，搜索的消息传输都会先编码
 * @Author:liwy
 * @date: 22.5.4
 * @Version:1.0
 *
 */

@Slf4j
public class RpcEncode<T> extends MessageToByteEncoder<MessageProtocol<T>> {

    /**
     * -----------------------------------------------------------
     * 魔数 2byte | 协议版本号 1byte | 序列化算法1byte |报文类型1byte|
     * -----------------------------------------------------------
     * 状态1byte  |消息id 32 byte  | 数据长度4 byte               |
     * -----------------------------------------------------------
     *                     数据内容                             |
     *-----------------------------------------------------------
     *
     * */

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageProtocol<T> messageProtocol, ByteBuf byteBuf) throws Exception {
        log.info("开始编码, messageProtocol : {}",messageProtocol);
        MessageHeader messageHeader = messageProtocol.getMessageHeader();
        // 魔数
        byteBuf.writeShort(messageHeader.getMagic());

        byteBuf.writeByte(messageHeader.getVersion());

        byteBuf.writeByte(messageHeader.getSerialization());

        byteBuf.writeByte(messageHeader.getMsgType());

        byteBuf.writeByte(messageHeader.getStatus());
        // 消息id为String ，utf-8编码
        byteBuf.writeCharSequence(messageHeader.getMessageId(), Charset.forName("UTF-8"));

        SerializationTypeEnum serializationTypeEnum = SerializationTypeEnum.parseByType(messageHeader.getSerialization());
        RpcSerialization rpcSerialization = SerializationStrategyFactory.getSerialization(serializationTypeEnum);
        byte[] data = rpcSerialization.serialize(messageProtocol.getBody());
        // 数据长度
        byteBuf.writeInt(data.length);
        //真实数据
        byteBuf.writeBytes(data);

    }



}
