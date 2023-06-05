package com.andyadc.bh.chat.message;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Message implements Serializable {

    public static final int LoginRequestMessage = 0;
    public static final int LoginResponseMessage = 1;
    public static final int ChatRequestMessage = 2;
    public static final int ChatResponseMessage = 3;
    public static final int GroupCreateRequestMessage = 4;
    public static final int GroupCreateResponseMessage = 5;
    public static final int GroupJoinRequestMessage = 6;
    public static final int GroupJoinResponseMessage = 7;
    public static final int GroupQuitRequestMessage = 8;
    public static final int GroupQuitResponseMessage = 9;
    public static final int GroupChatRequestMessage = 10;
    public static final int GroupChatResponseMessage = 11;
    public static final int GroupMembersRequestMessage = 12;
    public static final int GroupMembersResponseMessage = 13;

    private static final long serialVersionUID = 1994854448066129325L;

    private static final Map<Integer, Class<?>> messageClasses = new ConcurrentHashMap<>();

    static {
        messageClasses.put(LoginRequestMessage, LoginRequestMessage.class);
    }

    private int sequenceId;
    private int messageType;

    public static Class<?> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public abstract int getMessageType();

    @Override
    public String toString() {
        return "Message{" +
                "sequenceId=" + sequenceId +
                ", messageType=" + messageType +
                '}';
    }
}
