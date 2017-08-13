package com.github.willena.smsproxy.core.common.Messages;

import com.github.willena.smsproxy.core.common.CompressEngine.GenericCompressor;
import com.github.willena.smsproxy.core.common.Messages.HTTP.HTTPDataMessage;
import com.github.willena.smsproxy.core.common.Messages.HTTP.HTTPRequestMessage;
import com.github.willena.smsproxy.core.common.Messages.UserInteractMessage.SimpleMessage;
import com.github.willena.smsproxy.core.common.Messages.UserInteractMessage.UserInteractMessages;

/**
 * Created by Guillaume on 08/07/2017.
 */

public class MessageFactory {
    private static final String MESSAGE_IDENTIFIER_SUFIX = "++";
    private static final String MESSAGE_IDENTIFIER_PREFIX = "++";

    public static String formatMessage(Message message) {
        return MESSAGE_IDENTIFIER_PREFIX + message.toString() + MESSAGE_IDENTIFIER_SUFIX;
    }

    public static boolean isValidMessage(Message m) {
        return m.toString().matches("^\\+{2}.*\\+{2}$");
    }

    public static Message parseMessage(String message) {
        String content = message.substring(2, message.length() - 2);
        if (content.equals(UserInteractMessages.getUserConfirmationMessage()) ||
                content.equals(UserInteractMessages.getUserQueryMessage()) ||
                content.equals(UserInteractMessages.getUserStopQueryMessage())) {

            return SimpleMessage.parseMessage(content);

        }
        else
        {
            content = GenericCompressor.unCompressData(content);
            if( content.startsWith("R|"))
                return HTTPDataMessage.parseMessage(content);
            else if (content.startsWith("GET"))
                return HTTPRequestMessage.parseMessage(content);
            else if (content.startsWith("POST"))
                return HTTPRequestMessage.parseMessage(content);

        }
        return null;
    }


}
