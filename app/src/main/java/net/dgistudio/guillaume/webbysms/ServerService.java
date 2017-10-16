package net.dgistudio.guillaume.webbysms;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

import java.util.Date;

/**
 * Created by Guillaume on 13/05/2016.
 */
public class ServerService extends Service {

    public static final String INTENT_RECEIVE_COMMAND = "websms.intent.receive.service.command";
    public static final String COMMAND_GET_SERVER_STATS = "websms.commands.get.server.stats";
    public static final String COMMAND_STOP_SERVICE = "websms.commands.stop.service";
    public static final String INTENT_SEND_TO_SERVICE = "websms.intent.sendto.server.service";
    public static final String INTENT_SERVICE_COMMAND_ANSWER = "websms.service.command.answer";
    private static final String TAG = "SERVICE SMS";
    private boolean isRunning = false;
    private String contactNumber = "";
    private String contactName = "";

    private SMSContentSender smsContentSender;

    private StatsContainer serverStats = new StatsContainer();

    private IntentFilter filterSendToService = new IntentFilter(ServerService.INTENT_SEND_TO_SERVICE);
    private IntentFilter filterReceiveCommand = new IntentFilter(ServerService.INTENT_RECEIVE_COMMAND);

    private BroadcastReceiver informationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(INTENT_SEND_TO_SERVICE)) {
                long currentTime = new Date().getTime() / 1000;

                if (serverStats.getRemainingTime() <= 0) {
                    Log.d(TAG, "onReceive: Stoping service because ttl = " + (currentTime - serverStats.lastTime));
                    stopSelf();
                    return;
                }

                serverStats.smsReceived++;
                serverStats.setLastTime(currentTime);

                String message = intent.getStringExtra(SMSBroadcastReceiver.INTENT_IDENTIFIER_SMS_CONTENT);
                Log.d(TAG, "onReceive: " + message);
                String originalMessage = new Compressor().prepareAndDecompress(message);
                Log.d(TAG, "onReceive: decompressed : '" + originalMessage + "'");

                sendToActivity();
                Log.d(TAG, "onReceive: Sending data to activity");

                if (originalMessage.startsWith("R|")) {
                    handleResponseData(originalMessage);
                } else if (originalMessage.startsWith("GET") || originalMessage.startsWith("POST")) {
                    handleRequest(originalMessage);
                }


            }
        }
    };
    private BroadcastReceiver commandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: Get command !!! ");
            switch (intent.getStringExtra("command")) {
                case COMMAND_STOP_SERVICE:
                    stopSelf();
                    break;
                case COMMAND_GET_SERVER_STATS:
                    sendToActivity();
                    break;
                default:
            }
        }
    };

    private void handleRequest(String originalMessage) {
        Log.d(TAG, "onReceive: Starting getting requested page");

        Request request = Request.createFromString(originalMessage);

        Log.d(TAG, "handleRequest: request " + request.getMethod() + " " + request.getUrl());

        if (request != null) {
            AsyncTask<String, Void, RequestResponse> asyncTask = new PageDownloader(new AsyncResponse() {

                @Override
                public void processFinish(RequestResponse output) {

                    Log.d("MainActivity", "onCreate: html : " + output);
                    handleHtmlPage(output);
                }
            }).execute(request.getUrl());

            serverStats.requests.add(ParcelableRequest.createFromRequest(request));
        }
    }

    private void handleResponseData(String originalMessage) {
        Log.d(TAG, "handleResponseData: " + originalMessage);
    }

    private void handleHtmlPage(RequestResponse output) {
        //TODO: ------------DONE ! ----------------- COMPRESS USING : http://code.google.com/p/htmlcompressor/ html content !
        HtmlCompressor compressor = new HtmlCompressor();
        if (output != null) {
            Content content = new Content();
            String cookiesString = "";

            for (int i = 0; i < output.cookies.size(); i++) {
                cookiesString += CookieBuilder.cookieToString(output.cookies.get(i)) + ((i != output.cookies.size() - 1) ? "|" : "");
            }

            Log.d(TAG, "handleHtmlPage: CookieString" + cookiesString);

            String compressedHtml = compressor.compress(output.dataHtml);

            String message = "R|" + output.cookies.size() + "|" + cookiesString + "|" + compressedHtml;

            content.setMessage(message);

            Log.d(TAG, "handleHtmlPage: content :" + message);

            smsContentSender.sendContent(content);
            serverStats.smsSent++;
            sendToActivity();
        }
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        serverStats.lastTime = new Date().getTime() / 1000;
        isRunning = true;

        this.registerReceiver(informationReceiver, filterSendToService);
        this.registerReceiver(commandReceiver, filterReceiveCommand);

        smsContentSender = new SMSContentSender(this, contactNumber);


        Toast.makeText(getApplicationContext(), "Serveur service started", Toast.LENGTH_LONG).show();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR

        this.contactNumber = intent.getStringExtra(ServerServiceActivity.INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER);
        this.contactName = intent.getStringExtra(ServerServiceActivity.INTENT_IDENTIFIER_FROM_NOTY_CONTACTNAME);

        smsContentSender.setPhoneNumber(contactNumber);


        Log.d(TAG, "onStartCommand: " + contactNumber + " cName " + contactName);


        //stopSelf();

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        this.unregisterReceiver(informationReceiver);
        this.unregisterReceiver(commandReceiver);
        Log.i(TAG, "Service onDestroy");
        Toast.makeText(getApplicationContext(), "Serveur service stopped ", Toast.LENGTH_LONG).show();

    }

    private void sendToActivity() {
        Intent answer = new Intent(INTENT_SERVICE_COMMAND_ANSWER);
        answer.putExtra("data", serverStats);
        sendBroadcast(answer);
    }
}
