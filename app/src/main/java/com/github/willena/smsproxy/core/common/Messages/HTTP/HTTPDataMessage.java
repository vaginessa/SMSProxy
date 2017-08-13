package com.github.willena.smsproxy.core.common.Messages.HTTP;

import com.github.willena.smsproxy.core.common.Messages.MessageType;

import static com.github.willena.smsproxy.core.common.Messages.MessageType.HTTP_DATA;

/**
 * Created by Guillaume on 08/07/2017.
 */

public class HTTPDataMessage extends HTTPMessage {

    public HTTPDataMessage() {
        super(HTTP_DATA);
    }

    @Override
    public String toString() {
        return null;
    }
}
