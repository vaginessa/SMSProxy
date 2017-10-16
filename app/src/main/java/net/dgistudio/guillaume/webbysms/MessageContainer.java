package net.dgistudio.guillaume.webbysms;

import android.telephony.SmsMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 22/01/2016.
 */
public class MessageContainer {
    private String phoneNumber;
    private String message;
    private List<SmsMessage> messageList;

    public MessageContainer() {
        messageList = new ArrayList<>();
        phoneNumber = "";
        message = "";
    }

    public void appendMessage(String messagePart) {
        this.message += messagePart;
    }

    public void setSenderPhoneNumber(String number) {
        this.phoneNumber = number;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getMessage() {
        return this.message;
    }

    public void addMessage(SmsMessage msg) {
        this.messageList.add(msg);
    }

    public List<SmsMessage> getMessageList() {
        return this.messageList;
    }
}

