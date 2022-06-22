package com.mini.rpc.core.protocol;

import com.mini.rpc.core.serialization.SerializationTypeEnum;
import lombok.Data;

import java.util.UUID;

/**
 * @Author:liwy
 * @date: 22.5.1
 * @Version:1.0
 */
@Data
public class MessageHeader {

    /**
     * 定义消息头
     *
     *----------------------------------------------------------------
     * 魔数 2bytes |协议的版本号 1bytes | 序列化算法 1 bytes | 报文类型 1bytes|
     * ----------------------------------------------------------------
     *状态 1bytes | 消息id   32bytes   | 数据长度  4bytes               |
     * ----------------------------------------------------------------
     *
     * */

    private  short magic;

    private  byte version;

    private byte serialization;

    private  byte msgType;

    private byte status;

    private String messageId;

    private  int msgLen;


    public static MessageHeader bulid(String serialization) {
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMagic(ProtocolConstants.MAGIC);
        messageHeader.setMessageId(UUID.randomUUID().toString().replace("-", ""));
        messageHeader.setVersion(ProtocolConstants.VERSION);
        messageHeader.setMsgType(MsgType.REQUEST.getType());
        messageHeader.setSerialization(SerializationTypeEnum.parseByName(serialization).getType());
        return messageHeader;
    }


}
