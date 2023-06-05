package com.andyadc.bh.chat.server.session;

public class GroupSessionFactory {

    private static final GroupSession session = new GroupSessionMemoryImpl();

    public static GroupSession getGroupSession() {
        return session;
    }
}
