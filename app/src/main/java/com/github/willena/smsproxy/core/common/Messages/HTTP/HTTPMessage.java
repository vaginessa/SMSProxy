package com.github.willena.smsproxy.core.common.Messages.HTTP;

import com.github.willena.smsproxy.core.common.Messages.Message;
import com.github.willena.smsproxy.core.common.Messages.MessageType;


/**
 * Created by Guillaume on 09/07/2017.
 */

public abstract class HTTPMessage extends Message {

    public HTTPMessage(MessageType type) {
        super(type);
    }

    public static Message parseMessage(String message) {
        return Message.parseMessage(message);
    }

}
