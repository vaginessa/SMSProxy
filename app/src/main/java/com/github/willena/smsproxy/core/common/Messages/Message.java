package com.github.willena.smsproxy.core.common.Messages;

/**
 * Created by Guillaume on 09/07/2017.
 */

public abstract class Message {
    private MessageType messageType;
    private String phonenumber;

    public Message(MessageType type){
        messageType=type;
    }

    public static Message parseMessage(String message){
        throw new UnsupportedOperationException("Not Implemented,should be implemented in child classes");
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @Override
    public abstract String toString();
}
