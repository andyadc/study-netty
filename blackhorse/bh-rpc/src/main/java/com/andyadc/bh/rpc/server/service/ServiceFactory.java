package com.andyadc.bh.rpc.server.service;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceFactory {

    public static Properties properties;
    public static Map<Class<?>, Object> map = new ConcurrentHashMap<>();

    static {
        try (InputStream is = ServiceFactory.class.getResourceAsStream("/rpc.properties")) {
            properties = new Properties();
            properties.load(is);
            Set<String> names = properties.stringPropertyNames();
            for (String name : names) {
                if (name.endsWith("Service")) {
                    Class<?> interfaceClass = Class.forName(name);
                    Class<?> instanceClass = Class.forName(properties.getProperty(name));
                    map.put(interfaceClass, instanceClass.newInstance());
                }
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    // 根据 接口类 获取 实现类
    @SuppressWarnings({"unchecked"})
    public static <T> T getService(Class<T> interfaceClass) {
        return (T) map.get(interfaceClass);
    }
}
