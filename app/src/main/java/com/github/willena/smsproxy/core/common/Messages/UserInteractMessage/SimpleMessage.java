package com.github.willena.smsproxy.core.common.Messages.UserInteractMessage;

import com.github.willena.smsproxy.core.common.Messages.Message;
import com.github.willena.smsproxy.core.common.Messages.MessageType;

/**
 * Created by Guillaume on 09/07/2017.
 */

public class SimpleMessage extends Message {
    private String message;

    public SimpleMessage(String messsage) {
        super(MessageType.SIMPLE_MESSAGE);
        this.message = messsage;
    }

    public void appendToMessage(String append){
        this.message += append;
    }

    @Override
    public String toString() {
        return message;
    }
}
