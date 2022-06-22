package com.mini.rpc.core.serialization;

import com.caucho.hessian.io.HessianOutput;
import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import com.google.common.base.Preconditions;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.serializer.support.SerializationFailedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * @Author:liwy
 * @date: 22.5.2
 * @Version:1.0
 */
@Slf4j
public class HessianSerialization implements RpcSerialization {



    @Override
    public <T> byte[] serialize(T object) throws IOException {
        if (object == null) {
            throw new NullPointerException("serialize object is null");
        }
        byte [] bytes;
        HessianSerializerOutput hessianSerializerOutput;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            hessianSerializerOutput = new HessianSerializerOutput(byteArrayOutputStream);
            hessianSerializerOutput.writeObjectImpl(object);
            hessianSerializerOutput.flush();
            bytes = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            log.error("hessian序列化异常:",e);
            throw new SerializationFailedException(e.getMessage());
        }
        return bytes;
    }

    @Override
    public <T> T deSerialize(byte[] data, Class<T> clazz) throws IOException {
        Preconditions.checkNotNull(data,"deSerialize data is null");
        Preconditions.checkNotNull(clazz,"clazz is null");
        T result;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data)) {
            HessianSerializerInput hessianSerializerInput = new HessianSerializerInput(byteArrayInputStream);
            result = (T) hessianSerializerInput.readObject(clazz);
        } catch (Exception e) {
            log.error("hessians反序列化异常:", e);
            throw new SerializationFailedException(e.getMessage());
        }
        return result;
    }
}
