package com.github.willena.smsproxy.core.common.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.github.willena.smsproxy.core.common.Messages.Message;
import com.github.willena.smsproxy.core.common.Messages.MessageFactory;
import com.github.willena.smsproxy.core.common.Messages.UserInteractMessage.SimpleMessage;
import com.github.willena.smsproxy.core.common.Messages.UserInteractMessage.UserInteractMessages;

import java.util.List;

/**
 * Created by Guillaume on 09/07/2017.
 */

public class MainSMSReceiver extends BroadcastReceiver {

    public static final String INTENT_TRANSMIT_MESSAGE = "SMSProxy.intents.SMSReceiver.transmitMessage";
    public static final String EXTRA_MESSAGE_DATA = "SMSProxy.extra.message_data";
    private static final String TAG = "SMSReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        //this.abortBroadcast();
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                assert pdusObj != null;
                SimpleMessage currentSimpleMessage = new SimpleMessage("");
                for (Object aPdusObj : pdusObj) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);

                    currentSimpleMessage.setPhonenumber(currentMessage.getDisplayOriginatingAddress());
                    currentSimpleMessage.appendToMessage(currentMessage.getDisplayMessageBody());

                }

                if (MessageFactory.isValidMessage(currentSimpleMessage)) {
                    if (currentSimpleMessage.toString().equals(MessageFactory.formatMessage(UserInteractMessages.getUserQueryMessage()))) {
                        //TODO : Create notification and start client receiver onclick
                        Log.d(TAG, "onReceive:  received a query; now handshaking :D ");
                    }
                    else
                    {
                        Intent i = new Intent(INTENT_TRANSMIT_MESSAGE);
                        i.putExtra(EXTRA_MESSAGE_DATA, currentSimpleMessage.toString());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                    }
                }

            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }


}

