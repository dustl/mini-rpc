package com.mini.rpc.core.serialization;

import java.io.IOException;

/**
 * @Author:liwy
 * @date: 22.5.2
 * @Version:1.0
 */
public interface RpcSerialization {

    <T> byte[] serialize(T object) throws IOException;

    <T> T deSerialize(byte[] data, Class<T> clazz) throws IOException;
}
