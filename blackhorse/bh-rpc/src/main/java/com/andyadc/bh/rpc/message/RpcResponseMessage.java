package com.andyadc.bh.rpc.message;

public class RpcResponseMessage extends Message {

    private static final long serialVersionUID = 3424006383744043774L;

    private String error;
    private Object result;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public int getMessageType() {
        return 0;
    }
}
