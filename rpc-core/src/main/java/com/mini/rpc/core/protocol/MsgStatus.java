package com.mini.rpc.core.protocol;

import lombok.Getter;

/**
 * @Author:liwy
 * @date: 22.5.2
 * @Version:1.0
 */
public enum MsgStatus {
    SUCCESS((byte)0),
    FAIL((byte)1);

    @Getter
    private final byte code;

    MsgStatus(byte code) {
        this.code = code;
    }

    public static boolean isSuccess(byte code){
        return MsgStatus.SUCCESS.code == code;
    }

}
