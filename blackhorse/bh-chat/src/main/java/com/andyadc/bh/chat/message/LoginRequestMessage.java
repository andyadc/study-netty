package com.andyadc.bh.chat.message;

public class LoginRequestMessage extends Message {

    private static final long serialVersionUID = -4297256714020942252L;

    private String username;
    private String password;
    private String nickname;

    public LoginRequestMessage() {
    }

    public LoginRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }

    @Override
    public String toString() {
        return "LoginRequestMessage{" +
                "username=" + username +
                ", password=" + password +
                ", nickname=" + nickname +
                "} " + super.toString();
    }
}
