package net.dgistudio.guillaume.webbysms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;

public class ClientWebViewActivity extends AppCompatActivity {

    public static final String INTENT_IDENTIFIER_SMS_SENT_NUMBER = "websms.intent.extra.sms.sent.number";
    public static final String INTENT_IDENTIFIER_CONFIRMATION_RECEIVED = "websms.intent.extra.received.confirmationSMS";
    ProgressDialog progressDialog;
    SMSContentSender smsSender;
    CustomWebView webview;
    private SqlDbAccess dbAccess;
    private Button btnQueryPage;
    private EditText urlInput;
    private CookieSyncManager syncManager = null;
    private CookieManager cookieManager = null;

    private BroadcastReceiver informationReceiver = new BroadcastReceiver() {

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public void onReceive(Context c, Intent i) {
            webview.getSettings().setJavaScriptEnabled(true);
            Log.d("Receiver !", "RECU !!! ");
            if (i.getBooleanExtra(INTENT_IDENTIFIER_CONFIRMATION_RECEIVED, false)) {
                progressDialog.setProgress(3);
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Click anywhere to dismiss, You are receiving pages  from ...");

                checkCookieManager();


                Log.d("DONE", "ATTACH WEBVIEW overwrite");
            } else if (i.getBooleanExtra(SMSBroadcastReceiver.INTENT_IDENTIFIER_SMS_CONTENT_RECEIVED, false)) {
                String content = i.getStringExtra(SMSBroadcastReceiver.INTENT_IDENTIFIER_SMS_CONTENT);
                Log.d("DOoo ", "Content :" + content);

                String decoded = new Compressor().prepareAndDecompress(content);
                if (decoded == null) {
                    Log.d("CLIENT_ERROR_PACKET", "onReceive: ERROR WHILE DECOMPACKTING, ERROR IN PACKET ! SENDING MESSAGE TO GET a NEW ONE");

                    //TODO : SEND ERROR MESSAGE + RETURN FROM SERVER THE PAGE.
                } else if (decoded.length() != 0) {
                    Log.d("Client", "Page Content : " + decoded);
                    if (decoded.startsWith("R|")) {
                        String data = decoded.substring(2);
                        int nb = Integer.parseInt(data.substring(0, data.indexOf('|')));

                        checkCookieManager();

                        String htmlData = "";

                        if (nb == 0) {
                            htmlData = data.substring(3);
                        } else {
                            String[] mixedDataCookies = data.split("|", nb + 1);

                            Log.d("CLIENT !! !", "onReceive: cookiesList" + Arrays.toString(mixedDataCookies));


                            for (String cookie :
                                    mixedDataCookies) {
                                Log.d("COOKIE_DATA", "onReceive: cookie " + cookie);
                                CookieBuilder cookieBuilded = CookieBuilder.parseString(cookie);
                                cookieManager.setCookie(cookieBuilded.getDomain(), cookie);
                            }


                            htmlData = mixedDataCookies[mixedDataCookies.length - 1];

                        }


                        cookieManager.removeExpiredCookie();
                        syncManager.sync();

                        Log.d("HTMLDATA", "onReceive: Data html" + htmlData);

                        webview.loadData(htmlData, "text/html", "UTF-8");
                    }
                }
            }
        }
    };
    private BroadcastReceiver smsSentBCReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context c, Intent i) {
            Log.d("SMSSENT", "RECU 2");
            // Log.d("SMSSENT", i.getStringExtra(ClientWebViewActivity.INTENT_IDENTIFIER_FROM_NOTY_PHONENUMBER));
            Log.d("SMSSENT", "SMS NUMBER " + i.getIntExtra(ClientWebViewActivity.INTENT_IDENTIFIER_SMS_SENT_NUMBER, -1));

            if (getResultCode() == Activity.RESULT_OK) {
                progressDialog.setProgress(2);
                progressDialog.setTitle("Almost there...");
                progressDialog.setMessage("Waiting confirmation request...");
            }

        }
    };
    private IntentFilter filter = new IntentFilter(SMSBroadcastReceiver.INTENT_SEND_BACK_TO_ACTIVITY);
    private IntentFilter filterSMS = new IntentFilter(SMSBroadcastReceiver.INTENT_SMS_SENT);

    private void checkCookieManager() {

        if (syncManager == null || cookieManager == null) {
            syncManager = CookieSyncManager.createInstance(webview.getContext());
            cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeAllCookie();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webview = (CustomWebView) findViewById(R.id.WebView);
        // webview.addJavascriptInterface(new WebViewJavaScriptInterface(this, smsSender), "Interface");

        urlInput = (EditText) findViewById(R.id.pageUrl);

        btnQueryPage = (Button) findViewById(R.id.QueryBtnPage);
        btnQueryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.loadUrl(urlInput.getText().toString());
            }
        });
        dbAccess = new SqlDbAccess(this);
        dbAccess.open();

        this.registerReceiver(informationReceiver, filter);
        this.registerReceiver(smsSentBCReceiver, filterSMS);

        smsSender = new SMSContentSender(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        Sessions session = null;

        Log.d("WEBINTENT", " 2: " + action);

        long sessionId = intent.getLongExtra("sessionId", -1);
        if (sessionId == -1) {
            Log.d("WEBSMS1", "Quitting app NOw !!!");
        }
        Log.d("WEBVIEW", "" + sessionId);
        session = dbAccess.getSession(sessionId);


        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("sending request to " + session.getContactName());
        progressDialog.setTitle("Please wait ... ");
        progressDialog.setMax(3);
        //    progressDialog.show();

        //  smsSender.sendSimpleRequestSMS(session.getContactNumber());
        //  smsSender.setPhoneNumber(session.getContactNumber());

        //String postData = "username=my_username&password=my_password";
        //webview.postUrl("http://151.80.175.218/basicPage.php", EncodingUtils.getBytes(postData, "BASE64"));

        //webview.addJavascriptInterface(new WebViewJavaScriptInterface(this, smsSender), "Interface");

        webview.getSettings().setJavaScriptEnabled(true);

        //webview.setWebViewClient(new CustomWebViewClient(smsSender));

        webview.setWebViewClient(new InterceptingWebViewClient(this, webview));
        webview.setWebChromeClient(new WebChromeClient());

        //webview.setWebChromeClient(new WebViewChromeClient(smsSender));
        // webview.setWebViewClient(new CustomWebViewClient(smsSender));


    }



  /*  @Override
    protected void onResume() {
        this.registerReceiver(informationReceiver, filter);
        Log.d("ActivityWeb", "Resumed");
        super.onResume();
    }*/

  /*  @Override
    protected void onPause() {
        Log.d("ActivityWeb", "Resumed");
        this.unregisterReceiver(informationReceiver);
        super.onPause();
    }*/


}
