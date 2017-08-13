package com.github.willena.smsproxy.core.common.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.github.willena.smsproxy.core.common.Messages.Message;
import com.github.willena.smsproxy.core.common.Messages.MessageFactory;
import com.github.willena.smsproxy.core.common.receivers.MainSMSReceiver;

import static com.github.willena.smsproxy.core.common.receivers.MainSMSReceiver.EXTRA_MESSAGE_DATA;

/**
 * Created by Guillaume on 08/07/2017.
 */

public abstract class AbstractServiceDataManager extends Service {

    private BroadcastReceiver SMSreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String fullContent = intent.getStringExtra(MainSMSReceiver.EXTRA_MESSAGE_DATA);
            Message parseMessage = MessageFactory.parseMessage(fullContent);

            onMessageReceived(parseMessage);
        }
    };

    public abstract void onMessageReceived(Message message);

    public void sendMessage(Message message){

    }

    public void initCommunication(Context context){
        LocalBroadcastManager.getInstance(context).registerReceiver(SMSreceiver, new IntentFilter(MainSMSReceiver.INTENT_TRANSMIT_MESSAGE));
    }
}
