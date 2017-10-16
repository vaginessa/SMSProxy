package net.dgistudio.guillaume.webbysms;

/**
 * Created by Guillaume on 27/05/2016.
 * Inspired from https://github.com/KeejOow/android-post-webview/blob/master/PostWebview/postwebview/src/main/java/com/solidsoftware/postwebview/InterceptingWebViewClient.java
 */

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InterceptingWebViewClient extends WebViewClient {
    public static final String TAG = "IntercepWebViewClient";

    private Context mContext = null;
    private WebView mWebView = null;
    private PostInterceptJavascriptInterface mJSSubmitIntercept = null;
    private PostInterceptJavascriptInterface.FormRequestContents mNextFormRequestContents = null;
    private PostInterceptJavascriptInterface.AjaxRequestContents mNextAjaxRequestContents = null;

    public InterceptingWebViewClient(Context context, WebView webView) {
        mContext = context;
        mWebView = webView;
        mJSSubmitIntercept = new PostInterceptJavascriptInterface(this);
        mWebView.addJavascriptInterface(mJSSubmitIntercept, "interception");

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        mNextAjaxRequestContents = null;
        mNextFormRequestContents = null;

        view.loadUrl(url);
        return true;
    }

    public void onPageFinished(WebView view, String url) {
        injectScriptFile(view, "interceptheader.js");
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(final WebView view, final String url) {
        Log.d(TAG, "shouldInterceptRequest: OK link !");
        Log.d(TAG, "shouldInterceptRequest: Method post ? " + isPOST());

        if (isPOST()) {

            //TODO : We have All the informations we needs. Now it is time to send POSTs requests throught SMSs. the Request has cookies.
            //TODO : ELSE send a GET request whith cookies.



            if (mNextAjaxRequestContents != null) {
                Log.d(TAG, "writeForm: +++++++++++++++++++++++++++++++++++");
                Log.d(TAG, "writeForm: AJAX  : " + mNextAjaxRequestContents.body);

                Request r = new Request(url, Request.METHOD_POST);

                Log.d(TAG, "shouldInterceptRequest: data Request"+ r.toString());

                Log.d(TAG, "writeForm: +++++++++++++++++++++++++++++++++++");
            } else {
                Log.d(TAG, "writeForm: +++++++++++++++++++++++++++++++++++");
                Log.d(TAG, "writeForm: JSON : " + mNextFormRequestContents.json);
                Log.d(TAG, "writeForm: +++++++++++++++++++++++++++++++++++");

            }
        } else
        {
            Log.d(TAG, "shouldInterceptRequest: IS NOT POST !!! ");
            Request r = new Request(url, Request.METHOD_GET);
            Log.d(TAG, "shouldInterceptRequest: Req "+ r.toString());
        }


        mNextAjaxRequestContents = null;
        mNextFormRequestContents = null;

        return null;
    }

    private boolean isPOST() {
        return (mNextFormRequestContents != null || mNextAjaxRequestContents != null);
    }

    protected void writeForm(OutputStream out) {


        /*try {
            JSONArray jsonPars = new JSONArray(mNextFormRequestContents.json);

            // We assume to be dealing with a very simple form here, so no file uploads or anything
            // are possible for reasons of clarity
            FormEncoding.Builder m = new FormEncoding.Builder();
            for (int i = 0; i < jsonPars.length(); i++) {
                JSONObject jsonPar = jsonPars.getJSONObject(i);

                m.add(jsonPar.getString("name"), jsonPar.getString("value"));
                // jsonPar.getString("type");
                // TODO TYPE?
            }
            m.build().writeBodyTo(out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }

    public String getType(Uri uri) {
        String contentResolverUri = mContext.getContentResolver().getType(uri);
        if (contentResolverUri == null) {
            contentResolverUri = "*/*";
        }
        return contentResolverUri;
    }

    public void nextMessageIsFormRequest(PostInterceptJavascriptInterface.FormRequestContents formRequestContents) {
        Log.d(TAG, "nextMessageIsFormRequest: FORM REQUEST !!!!! ");
        mNextFormRequestContents = formRequestContents;
    }

    public void nextMessageIsAjaxRequest(PostInterceptJavascriptInterface.AjaxRequestContents ajaxRequestContents) {
        Log.d(TAG, "nextMessageIsAjaxRequest: AJAXRequest !!! ");
        mNextAjaxRequestContents = ajaxRequestContents;
    }

    private void injectScriptFile(WebView view, String scriptFile) {
        InputStream input;
        try {
            input = view.getContext().getAssets().open(scriptFile);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            view.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
