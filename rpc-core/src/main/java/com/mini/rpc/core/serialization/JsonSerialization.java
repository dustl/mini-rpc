package com.mini.rpc.core.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * @Author:liwy
 * @date: 22.5.2
 * @Version:1.0
 */
public class JsonSerialization implements RpcSerialization {

    public static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = getObjectMapper(JsonInclude.Include.ALWAYS);
    }

    private static ObjectMapper getObjectMapper(JsonInclude.Include include ) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(include);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;

    }


    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return object instanceof String ? ((String) object).getBytes() : OBJECT_MAPPER.writeValueAsString(object).getBytes();
    }

    @Override
    public <T> T deSerialize(byte[] data, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(Arrays.toString(data), clazz);
    }
}
