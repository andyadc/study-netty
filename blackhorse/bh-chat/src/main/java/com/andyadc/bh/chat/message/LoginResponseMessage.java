package com.andyadc.bh.chat.message;

public class LoginResponseMessage extends AbstractResponseMessage {

    private static final long serialVersionUID = -1663669799973031202L;

    public LoginResponseMessage(boolean succ) {
        super.setSuccess(succ);
    }

    public LoginResponseMessage(boolean succ, String error) {
        super.setSuccess(succ);
        super.setError(error);
    }

    @Override
    public int getMessageType() {
        return LoginResponseMessage;
    }

    @Override
    public String toString() {
        return "LoginResponseMessage{} " + super.toString();
    }
}
