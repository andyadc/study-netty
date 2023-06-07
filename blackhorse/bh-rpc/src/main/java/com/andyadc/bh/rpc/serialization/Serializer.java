package com.andyadc.bh.rpc.serialization;

import com.alibaba.fastjson2.JSON;

public interface Serializer {

    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] data, Class<T> clazz);

    enum Type implements Serializer {

        Jdk {
            @Override
            public <T> byte[] serialize(T obj) {
                return new byte[0];
            }

            @Override
            public <T> T deserialize(byte[] data, Class<T> clazz) {
                return null;
            }
        },

        Json {
            @Override
            public <T> byte[] serialize(T obj) {
                return JSON.toJSONBytes(obj);
            }

            @Override
            public <T> T deserialize(byte[] data, Class<T> clazz) {
                return JSON.parseObject(data, clazz);
            }
        }
    }
}
