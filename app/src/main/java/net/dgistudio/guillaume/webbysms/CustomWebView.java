package net.dgistudio.guillaume.webbysms;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

public class CustomWebView extends WebView {
    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void loadUrl(String url) {
        System.out.println("+++++WebView loadUrl:" + url);
        super.loadUrl(url);

    }

    @Override
    public void postUrl(String url, byte[] postData) {
        System.out.println("+++++++WebView postUrl:" + url);
        super.postUrl(url, postData);
    }
}
