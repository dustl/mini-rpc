package com.mini.rpc.core.serialization;

/**
 * @Author:liwy
 * @date: 22.6.23
 * @Version:1.0
 */
public class SerializationStrategyFactory {

    public static RpcSerialization getSerialization(SerializationTypeEnum typeEnum) {
        switch (typeEnum) {
            case JSON:
                return new JsonSerialization();
            case HESSIAN:
                return new HessianSerialization();
            case KRYO:
                return new KryoSerialization();
            default:
                throw new IllegalArgumentException("序列化类型不支持");

        }

    }



}
