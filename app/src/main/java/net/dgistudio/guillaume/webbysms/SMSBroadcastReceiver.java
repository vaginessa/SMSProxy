package net.dgistudio.guillaume.webbysms;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.List;

/**
 * Created by Guillaume on 15/01/2016.
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {

    public static final int NOTIFICATION_ID = 1;
    public static final String INTENT_SEND_BACK_TO_ACTIVITY = "websms.intent.loopBack.webView";
    public static final String INTENT_SMS_SENT = "websms.intent.loopBack.sms.sent";
    public static final String INTENT_IDENTIFIER_SMS_CONTENT_RECEIVED = "websms.intent.extra.sms.content.received";
    public static final String INTENT_IDENTIFIER_SMS_CONTENT = "websms.intent.extra.sms.content";
    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        //this.abortBroadcast();
        final Bundle bundle = intent.getExtras();

        Intent smsRecvIntent = new Intent("android.provider.Telephony.SMS_RECEIVED");
        List<ResolveInfo> infos = context.getPackageManager().queryBroadcastReceivers(smsRecvIntent, 0);
        for (ResolveInfo info : infos) {
            Log.d("JGDUEDNOIN", "Receiver: " + info.activityInfo.name + ", priority=" + info.priority);
        }

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                assert pdusObj != null;
                MessageContainer container = new MessageContainer();
                for (Object aPdusObj : pdusObj) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    container.addMessage(currentMessage);

                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    container.setSenderPhoneNumber(senderNum);

                    String message = currentMessage.getDisplayMessageBody();
                    Log.d("Message", "Mes : " + message);
                    container.appendMessage(message);


                } // end for loop

                this.analyzeMessage(context, container);

            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }

    private void analyzeMessage(Context context, MessageContainer container) {

        String message = container.getMessage();
        String senderNum = container.getPhoneNumber();

        Log.d("Message", "MES 2 --> " + message);
        if (message.matches("^\\+{2}WebSMS : Request\\+{2}$")) {
            Log.i("SmsReceiver", "______________________________________________________");
            Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
            Log.i("SmsReceiver", "______________________________________________________");

            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(senderNum));
            Cursor cr = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle("WebSMS Request");
            mBuilder.setVibrate(new long[]{500, 500, 1000});
            mBuilder.setLights(Color.rgb(51, 193, 219), 1500, 1500);

            Intent resultIntent = new Intent(context, ServerServiceActivity.class);
            resultIntent.setAction(ServerServiceActivity.ACTION_LOAD_NOTY);
            resultIntent.putExtra(ServerServiceActivity.INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER, senderNum);


            mBuilder.setContentText("You have one request from " + senderNum + " ");
            String name = senderNum;
            assert cr != null;
            if (cr.moveToFirst()) {
                name = cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                mBuilder.setContentText("You have one request from " + name + " ");
            }
            resultIntent.putExtra(ServerServiceActivity.INTENT_IDENTIFIER_FROM_NOTY_CONTACTNAME, name);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(ServerServiceActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        } else if (message.matches("^\\+{2}WebSMS : Confirmation Request\\+{2}$")) {
            Log.d("Debug", "Confirmation request SMS receiver");
            Intent sendByBroadCast = new Intent();
            sendByBroadCast.setAction(SMSBroadcastReceiver.INTENT_SEND_BACK_TO_ACTIVITY);
            sendByBroadCast.putExtra(ServerServiceActivity.INTENT_IDENTIFIER_CONFIRMATION_RECEIVED, true);
            sendByBroadCast.putExtra(ServerServiceActivity.INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER, senderNum);
            context.sendBroadcast(sendByBroadCast);
        } else if (message.matches("^\\+{2}.*\\+{2}$")) // This message is content
        {
            Log.d("Debug", "SMS match content");
            Intent sendByBroadCast = new Intent();
            sendByBroadCast.setAction(SMSBroadcastReceiver.INTENT_SEND_BACK_TO_ACTIVITY);
            sendByBroadCast.putExtra(INTENT_IDENTIFIER_SMS_CONTENT_RECEIVED, true);
            sendByBroadCast.putExtra(INTENT_IDENTIFIER_SMS_CONTENT, message.substring(2, message.length() - 2));
            context.sendBroadcast(sendByBroadCast);

            if (isServiceRunning(ServerService.class, context)) {
                sendByBroadCast.setAction(ServerService.INTENT_SEND_TO_SERVICE);
                //sendByBroadCast.putExtra(ServerServiceActivity.INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER, senderNum);
                context.sendBroadcast(sendByBroadCast);
            }

        }

    }

    private boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
