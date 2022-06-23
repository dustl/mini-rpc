package com.mini.rpc.core.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author:liwy
 * @date: 22.5.2
 * @Version:1.0
 */
@Data
public class MessageProtocol<T> implements Serializable {


    /**
     * 消息头
     * */
    private MessageHeader messageHeader;

    /**
     * 消息体（存放消息内容）
     * */
    private T body;

}
