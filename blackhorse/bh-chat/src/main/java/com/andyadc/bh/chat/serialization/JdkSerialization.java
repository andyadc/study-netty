package com.andyadc.bh.chat.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JdkSerialization implements Serialization {

    @Override
    public <T> byte[] serialize(T obj) {
        if (obj == null) {
            throw new RuntimeException("serialize object is null");
        }
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
    public <T> T deserialize(byte[] data, Class<T> cls) {
        if (data == null) {
            throw new RuntimeException("deserialize data is null");
        }
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
}
