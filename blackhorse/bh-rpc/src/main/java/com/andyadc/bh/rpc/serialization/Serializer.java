package com.andyadc.bh.rpc.serialization;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface Serializer {

    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] data, Class<T> clazz);

    enum Type implements Serializer {

        Jdk {
            @Override
            public <T> byte[] serialize(T obj) {
                ObjectOutputStream out = null;
                try {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    out = new ObjectOutputStream(os);
                    out.writeObject(obj);
                    return os.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(), e);
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                }
            }

            @SuppressWarnings({"unchecked"})
            @Override
            public <T> T deserialize(byte[] data, Class<T> clazz) {
                ObjectInputStream in = null;
                try {
                    ByteArrayInputStream is = new ByteArrayInputStream(data);
                    in = new ObjectInputStream(is);
                    return (T) in.readObject();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        },

        Json {
            @Override
            public <T> byte[] serialize(T obj) {
                return JSON.toJSONBytes(obj);
            }

            @Override
            public <T> T deserialize(byte[] data, Class<T> clazz) {
                return JSON.parseObject(data, clazz, JSONReader.Feature.SupportClassForName);
            }
        }
    }
}
