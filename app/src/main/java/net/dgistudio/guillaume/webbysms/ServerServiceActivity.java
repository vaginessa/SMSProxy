package net.dgistudio.guillaume.webbysms;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class ServerServiceActivity extends AppCompatActivity {

    public static final String ACTION_LOAD_NOTY = "websms.action.loadFromNoty";
    public static final String INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER = "websms.intent.extra.id.phoneNumber";
    public static final String INTENT_IDENTIFIER_FROM_NOTY_CONTACTNAME = "websms.intent.extra.id.contactName";
    public static final String INTENT_IDENTIFIER_CONFIRMATION_RECEIVED = "websms.intent.extra.received.confirmationSMS";
    private static final String TAG = "ServerServiceActivity";
    Timer timer;
    ProgressDialog progressDialog;
    SMSContentSender smsSender;
    private SqlDbAccess dbAccess;
    private ListView listRequest;
    private TextView valueSMSReceived, valueSMSSent, valueTime;
    private String contactNumber;
    private String contactName;
    private Boolean started = false;
    private IntentFilter filter = new IntentFilter(SMSBroadcastReceiver.INTENT_SEND_BACK_TO_ACTIVITY);
    private IntentFilter filterCommandCallback = new IntentFilter(ServerService.INTENT_SERVICE_COMMAND_ANSWER);

    private BroadcastReceiver informationReceiver = new BroadcastReceiver() {

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public void onReceive(Context c, Intent i) {
            Log.d("Receiver !", "RECU !!! ");
            if (i.getBooleanExtra(INTENT_IDENTIFIER_CONFIRMATION_RECEIVED, false)) {
                started = true;
                progressDialog.setProgress(3);
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Click anywhere to dismiss, You are sending pages to " + i.getStringExtra(INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER));

                Log.d("DONE", "Start Service ! ");
                Log.d(TAG, "onReceive: PhoneNumer : " + contactNumber);
                Log.d(TAG, "onReceive: Name : " + contactName);
                Log.d(TAG, "onReceive: PhoneNumberA : " + i.getStringExtra(INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER));

                if (!isServiceRunning(ServerService.class)) {
                    Log.d(TAG, "onReceive: Service not running ! Starting it !");
                    Intent intent = new Intent(ServerServiceActivity.this, ServerService.class);
                    intent.putExtra(ServerServiceActivity.INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER, contactNumber);
                    intent.putExtra(ServerServiceActivity.INTENT_IDENTIFIER_FROM_NOTY_CONTACTNAME, contactName);
                    startService(intent);


                } else {
                    Log.d(TAG, "onReceive: Service is already running");
                }

                stopTimer();
                startTimer();

            }
        }


    };

    private BroadcastReceiver commandCallbackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            StatsContainer stat = intent.getParcelableExtra("data");
//            Log.d(TAG, "onReceive: data stat");
//            Log.d(TAG, "onReceive: smsSent : " + stat.getSmsSent());
//            Log.d(TAG, "onReceive: remainingTime : " + stat.getRemainingTime());
//            Log.d(TAG, "onReceive: smsReveived : " + stat.getSmsReceived());

            if (stat.getRemainingTime() <= 0) {
                sendStopService();
                finish();
                return;
            }

            RequestToListViewAdapter adapter = new RequestToListViewAdapter(ServerServiceActivity.this, stat.getRequests());
            listRequest.setAdapter(adapter);
            valueTime.setText(String.valueOf(stat.getRemainingTime()));
            valueSMSSent.setText(String.valueOf(stat.getSmsSent()));
            valueSMSReceived.setText(String.valueOf(stat.getSmsReceived()));
            Log.d(TAG, "onReceive: Done changes ! ");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_service);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                if (isServiceRunning(ServerService.class)) {
                    sendStopService();
                    finish();
                }
            }
        });


        listRequest = (ListView) findViewById(R.id.listRequest);
        valueSMSSent = (TextView) findViewById(R.id.valueSMSSent);
        valueTime = (TextView) findViewById(R.id.valueTime);
        valueSMSReceived = (TextView) findViewById(R.id.valueSMSReceived);

        dbAccess = new SqlDbAccess(this);
        dbAccess.open();

        this.registerReceiver(informationReceiver, filter);
        this.registerReceiver(commandCallbackReceiver, filterCommandCallback);

        smsSender = new SMSContentSender(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        Sessions session;

        Log.d("ServiceActivity", " 2: " + action);

        NotificationManager nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nMgr.cancel(SMSBroadcastReceiver.NOTIFICATION_ID);

        Log.d("LOG", intent.getStringExtra(INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER));
        Log.d("LOG", intent.getStringExtra(INTENT_IDENTIFIER_FROM_NOTY_CONTACTNAME));
        Log.d("LOG", String.valueOf(dbAccess.searchByPhoneNumber(intent.getStringExtra(INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER))));

        if (!dbAccess.searchByPhoneNumber(intent.getStringExtra(INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER))) {

            session = dbAccess.createNewSession(intent.getStringExtra(INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER),
                    intent.getStringExtra(INTENT_IDENTIFIER_FROM_NOTY_CONTACTNAME),
                    intent.getStringExtra(INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER), false);
            Log.d(TAG, "onCreate: INTENT" + intent.getStringExtra(INTENT_IDENTIFIER_FROM_NOTY_CONTACTNAME) + " " + intent.getStringExtra(INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER));

        } else {
            session = dbAccess.findSessionByPhoneNumber(intent.getStringExtra(INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER));
        }


        contactName = session.getContactName();
        contactNumber = session.getContactNumber();

        if (!started) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("sending request to " + session.getContactName());
            progressDialog.setTitle("Please wait ... ");
            progressDialog.setMax(3);
            progressDialog.show();

            if (intent.getAction().equals(ServerServiceActivity.ACTION_LOAD_NOTY)) {
                progressDialog.setProgress(2);
                progressDialog.setTitle("Almost there...");
                progressDialog.setMessage("Sending confirmation request...");
                smsSender.sendSimpleConfirmationRequest(session.getContactNumber());
                smsSender.setPhoneNumber(session.getContactNumber());
            } else {
                smsSender.sendSimpleRequestSMS(session.getContactNumber());
                smsSender.setPhoneNumber(session.getContactNumber());
            }

            progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    progressDialog.getContext().unregisterReceiver(informationReceiver);
                }
            });
        }

    }

    private void sendStopService() {
        Intent intent = new Intent(ServerService.INTENT_RECEIVE_COMMAND);
        intent.putExtra("command", ServerService.COMMAND_STOP_SERVICE);

        sendBroadcast(intent);
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "run: 1s");
                sendCommandUpdate();
            }
        }, 0, 1000);//Update text every second
    }

    private void sendCommandUpdate() {
        Intent intent = new Intent(ServerService.INTENT_RECEIVE_COMMAND);
        intent.putExtra("command", ServerService.COMMAND_GET_SERVER_STATS);
        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //this.registerReceiver(informationReceiver, filter);
        stopTimer();
        startTimer();

        sendCommandUpdate();
        //  Log.d("ActivityWeb", "Resumed");

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    private void stopTimer() {
        if (timer != null)
            timer.cancel();
        timer = null;
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
