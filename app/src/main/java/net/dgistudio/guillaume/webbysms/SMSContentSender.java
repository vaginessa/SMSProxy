package net.dgistudio.guillaume.webbysms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.ArrayList;

/**
 * Created by Guillaume on 16/01/2016.
 */
public class SMSContentSender {


    SmsManager smsManager = SmsManager.getDefault();
    private Context AppContext;
    private String phoneNumber;

    public SMSContentSender(Context context, String phoneNumber) {
        this.AppContext = context;
        this.phoneNumber = phoneNumber;
    }
    public SMSContentSender(Context context) {
        this.AppContext = context;
        this.phoneNumber = "";
    }

    public void sendSimpleRequestSMS(String phoneNumber) {
        Intent sentIntent = new Intent(SMSBroadcastReceiver.INTENT_SMS_SENT);
        sentIntent.putExtra(ServerServiceActivity.INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER, phoneNumber);
        PendingIntent sentPI = PendingIntent.getBroadcast(AppContext.getApplicationContext(), 0, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        smsManager.sendTextMessage(phoneNumber, null, "++WebSMS : Request++", sentPI, null);

    }

    public Context getContext()
    {
        return this.AppContext;
    }

    public void sendSimpleConfirmationRequest(String phoneNumber) {

        Intent sentIntent = new Intent(SMSBroadcastReceiver.INTENT_SMS_SENT);
        sentIntent.putExtra(ServerServiceActivity.INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER, phoneNumber);
        PendingIntent sentPI = PendingIntent.getBroadcast(AppContext.getApplicationContext(), 0, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        smsManager.sendTextMessage(phoneNumber, null, "++WebSMS : Confirmation Request++", sentPI, null);
    }

    public void sendContent(Content content)
    {
        String message = "++"+content.compressMessage()+"++";
        ArrayList<String> parts = smsManager.divideMessage(message);

        ArrayList<PendingIntent> sentIntents = new ArrayList<>();
        int i;
        for (i=0;i<parts.size();i++)
        {
            Intent sentIntent = new Intent(SMSBroadcastReceiver.INTENT_SMS_SENT);
            sentIntent.putExtra(ClientWebViewActivity.INTENT_IDENTIFIER_SMS_SENT_NUMBER, i);
            sentIntent.putExtra(ServerServiceActivity.INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER, this.phoneNumber);
            PendingIntent sentPI = PendingIntent.getBroadcast(AppContext.getApplicationContext(), 0, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            sentIntents.add(sentPI);
        }


        smsManager.sendMultipartTextMessage(this.phoneNumber, null, parts, sentIntents, null);

    };

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
