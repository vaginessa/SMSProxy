package com.github.willena.smsproxy.core.common.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;

import com.github.willena.smsproxy.Utils.DataEXchanger;
import com.github.willena.smsproxy.core.common.Messages.Message;
import com.github.willena.smsproxy.core.common.Messages.MessageFactory;

import java.util.ArrayList;

/**
 * Created by guill on 12/08/2017.
 */

public class SMSEmmiterService extends Service {

    SmsManager smsManager = SmsManager.getDefault();

    //TODO : Service needs to be registered, and broadcast too.

    private BroadcastReceiver cmdSendSMS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getExtras().getString("EXchangerKey", null);
            Message message = (Message) DataEXchanger.getData(key);

            sendMessage(message);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void sendMessage(Message message){
        ArrayList<String> parts = smsManager.divideMessage(MessageFactory.formatMessage(message));
        smsManager.sendMultipartTextMessage(message.getPhonenumber(), null, parts, null, null);
    }
}
