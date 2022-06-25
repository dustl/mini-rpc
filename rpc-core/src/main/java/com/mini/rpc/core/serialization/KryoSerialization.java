package com.mini.rpc.core.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mini.rpc.core.common.RpcRequest;
import com.mini.rpc.core.common.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.serializer.support.SerializationFailedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author:liwy
 * @date: 22.6.24
 * @Version:1.0
 */
@Slf4j
public class KryoSerialization implements RpcSerialization{
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             Output output = new Output(os)) {

            Kryo kryo = new Kryo();
            kryo.register(RpcRequest.class);
            kryo.register(RpcResponse.class);
            // 对象转换成byte数组
            kryo.writeObject(output, kryo);
            return output.toBytes();
        } catch (Exception e) {
            log.error("kryo 序列化异常：", e);
            throw new SerializationFailedException(e.getMessage());
        }

    }

    @Override
    public <T> T deSerialize(byte[] data, Class<T> clazz) throws IOException {
        try (ByteArrayInputStream is = new ByteArrayInputStream(data);
             Input input = new Input(is)) {
            Kryo kryo = new Kryo();
            kryo.register(RpcResponse.class);
            kryo.register(RpcRequest.class);
            T t = kryo.readObject(input, clazz);
            return clazz.cast(t);
        } catch (Exception e) {
            log.error("kryo 反序列化失败 : ", e);
            throw new SerializationFailedException(e.getMessage());
        }
    }
}
