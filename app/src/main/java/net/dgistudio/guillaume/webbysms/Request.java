package net.dgistudio.guillaume.webbysms;

import android.util.Log;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by Guillaume on 22/01/2016.
 */
public class Request {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    private static final String TAG = "REQUEST";
    private final String url;
    private final String method;
    private ArrayList<String> postParam;
    private ArrayList<CookieBuilder> cookiesList;


    public Request(String url) {
        cookiesList = new ArrayList<>();
        this.method = this.METHOD_GET;
        this.url = url;
    }

    public Request(String url, String method) {
        cookiesList = new ArrayList<>();
        this.method = method;
        this.url = url;
    }


    public static Request createFromString(String string) {
        //String has to be like : METHOD|PAGE|PARAM|cookies...|cookie ...|

        String[] mixedSplit = string.split(Pattern.quote("|"), 3);
        String TAG = "REQUEST_CONV";
        Log.d(TAG, "createFromString: base String" + string);
        Log.d(TAG, "createFromString: " + Arrays.toString(mixedSplit));

        if (mixedSplit.length > 0) {
            Request r = new Request(mixedSplit[1], mixedSplit[0]);

            String[] cookiesStrings = mixedSplit[3].split(Pattern.quote("|"));

            for (String cookie :
                    cookiesStrings) {
                r.addCookie(CookieBuilder.parseString(cookie));
            }

            return r;
        }
        return null;

    }

    public Content toContent() {
        Content content = new Content();

        content.setMessage(this.toString());

        return content;
    }

    public String getMethod() {
        return this.method;
    }

    public String getUrl() {
        return url;
    }

    public void addCookie(CookieBuilder cookie) {
        cookiesList.add(cookie);
    }

    public String toString() {
        String cookieString = "";
        for (int i = 0; i < cookiesList.size(); i++) {
            cookieString += CookieBuilder.cookieToString(cookiesList.get(0)) + ((i != cookiesList.size() - 1) ? "|" : "");
        }

        return this.method + "|" + this.url + "|" + StringUtil.join(postParam, "&") + "|" + cookieString;
    }

    public ArrayList<String> getPostParam() {
        return postParam;
    }

    public void addPostParam(String name, String value) {
        if (!getMethod().equals(METHOD_POST))
            return;

        String data = name + "=" + value;
        Log.d(TAG, "addPostParam: data post" + data);
        postParam.add(data);
    }
}
