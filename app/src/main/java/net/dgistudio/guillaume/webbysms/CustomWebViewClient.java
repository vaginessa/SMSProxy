package net.dgistudio.guillaume.webbysms;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Guillaume on 16/01/2016.
 */
public class CustomWebViewClient extends WebViewClient {

    SMSContentSender smsSender;

    ConnectivityManager connManager;
    NetworkInfo activeNetwork;

    public CustomWebViewClient(SMSContentSender smsSender) {
        this.smsSender = smsSender;
        Log.d("WEBVIEWCLIENT", "Custom Web View Client");

        connManager = (ConnectivityManager) smsSender.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        connManager.getActiveNetworkInfo();

    }


    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);

        Log.d("WEBCLIEN", "Should verride ?" + url);
        return true;
    }

    //Show loader on url load
    public void onLoadResource(WebView view, String url) {

        Log.d("WEBCLIEN", "INFOR : LOADING !" + url);
        if (!( activeNetwork != null && activeNetwork.isConnectedOrConnecting()))
        {
            CookieSyncManager.createInstance(view.getContext());
            String cookieString = CookieManager.getInstance().getCookie(url);
            Log.d("CUSTOM_WEB_CLIENT", "onLoadResource: " + cookieString);
                smsSender.sendContent(new Request(url).toContent());
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(smsSender.getContext());
            builder.setMessage("You can't use this app with an active internet connection !")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            // Create the AlertDialog object and return it
            builder.create().show();

        }


    }


    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        view.stopLoading();
    }

    public void onPageFinished(WebView view, String url) {
        Log.d("WEBCLIEN", "INFOR : FINIHED ! !");
        // view.loadUrl("javascript:window.Interface.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>')");
    }

}
