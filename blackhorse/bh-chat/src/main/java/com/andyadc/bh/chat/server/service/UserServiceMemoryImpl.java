package com.andyadc.bh.chat.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserServiceMemoryImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceMemoryImpl.class);

    private static final Map<String, String> USER_MAP = new ConcurrentHashMap<>();

    static {
        USER_MAP.put("zhangsan", "123");
        USER_MAP.put("lisi", "123");
        USER_MAP.put("wangwu", "123");
        USER_MAP.put("zhangdeshuai", "123");
    }

    @Override
    public boolean login(String username, String password) {
        logger.info("username: {}", username);
        String pass = USER_MAP.get(username);
        return pass.equals(password);
    }
}
