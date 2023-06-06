package com.andyadc.bh.chat.message;

public abstract class AbstractResponseMessage extends Message {

    private static final long serialVersionUID = 191381680796394339L;

    private boolean success;
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return " {" +
                "success=" + success +
                ", error=" + error +
                "} " + super.toString();
    }
}
