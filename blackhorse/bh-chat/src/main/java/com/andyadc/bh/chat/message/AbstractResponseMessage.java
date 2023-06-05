package com.andyadc.bh.chat.message;

public abstract class AbstractResponseMessage extends Message {

    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
