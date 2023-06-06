package com.andyadc.bh.chat.serialization;

public interface Serialization {

    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] data, Class<T> clazz);

    enum Type implements Serialization {

        JDK {
            @Override
            public <T> byte[] serialize(T obj) {
                return new byte[0];
            }

            @Override
            public <T> T deserialize(byte[] data, Class<T> clazz) {
                return null;
            }
        },

        JSON {
            @Override
            public <T> byte[] serialize(T obj) {
                return new byte[0];
            }

            @Override
            public <T> T deserialize(byte[] data, Class<T> clazz) {
                return null;
            }
        }
    }
}
