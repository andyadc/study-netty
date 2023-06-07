package com.andyadc.bh.chat.message;

public abstract class AbstractResponseMessage extends Message {

    private static final long serialVersionUID = -6224736913638259671L;

    private boolean success;
    private String error;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
                ", message=" + message +
                "} " + super.toString();
    }
}
