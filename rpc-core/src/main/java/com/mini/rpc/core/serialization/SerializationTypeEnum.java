package com.mini.rpc.core.serialization;

import com.esotericsoftware.kryo.Kryo;
import lombok.Getter;

/**
 * @Author:liwy
 * @date: 22.5.1
 * @Version:1.0
 */
public enum SerializationTypeEnum {

    KRYO((byte)0),
    HESSIAN((byte) 1),
    JSON((byte) 2),
    ;

    @Getter
    private byte type;

    SerializationTypeEnum(byte type) {
        this.type = type;
    }

    public static SerializationTypeEnum parseByName(String typeName) {
        for (SerializationTypeEnum typeEnum : SerializationTypeEnum.values()) {
            if (typeEnum.name().equalsIgnoreCase(typeName)) {
                return typeEnum;
            }
        }
        return KRYO;
    }

    public static SerializationTypeEnum parseByType(byte type) {
        for (SerializationTypeEnum typeEnum : SerializationTypeEnum.values()) {
            if (typeEnum.getType() == type) {
                return typeEnum;
            }
        }
        return HESSIAN;
    }

}
