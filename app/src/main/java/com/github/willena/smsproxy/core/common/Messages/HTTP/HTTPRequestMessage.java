package com.github.willena.smsproxy.core.common.Messages.HTTP;

import android.text.TextUtils;

import com.github.willena.smsproxy.core.common.HTTPComponent.Cookie;
import com.github.willena.smsproxy.core.common.HTTPComponent.HTTPMethod;
import com.github.willena.smsproxy.core.common.Messages.Message;
import com.github.willena.smsproxy.core.common.Messages.MessageType;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.github.willena.smsproxy.core.common.Messages.MessageType.HTTP_REQUEST;

/**
 * Created by Guillaume on 09/07/2017.
 */

public class HTTPRequestMessage extends HTTPMessage {

    /**
     * An SMS HTTP Request is built this way
     * <p>
     * METHOD|PAGE|PARAM|Cookie 1...|cookie 2..|
     * ex: GET|http://google.fr/|||
     * ex: POST|http://dgiteam.party/test.php|test=azert&age=26||
     */

    private HTTPMethod method;
    private String url;
    private ArrayList<String> postParam;
    private ArrayList<Cookie> cookiesList;

    {
        postParam = new ArrayList<>();
        cookiesList = new ArrayList<>();
    }

    public HTTPRequestMessage(HTTPMethod method) {
        super(HTTP_REQUEST);
        this.method = method;
    }

    public HTTPRequestMessage(HTTPMethod method, String url) {
        this(method);
        this.url = url;
    }

    public static Message parseMessage(String message) {
        String[] messageParts = message.split(Pattern.quote("|"), 3);

        if (messageParts.length > 0) {
            HTTPRequestMessage r = new HTTPRequestMessage(HTTPMethod.valueOf(messageParts[1]), messageParts[0]);


            String[] cookiesStrings = messageParts[3].split(Pattern.quote("|"));

            for (String cookie : cookiesStrings) {
                r.getCookiesList().add(Cookie.parseString(cookie));
            }

            return r;
        }
        return null;
    }

    public ArrayList<Cookie> getCookiesList() {
        return cookiesList;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public ArrayList<String> getPostParam() {
        return postParam;
    }

    public String getUrl() {
        return url;
    }

    public void setMethod(HTTPMethod method) {
        this.method = method;
    }


    @Override
    public String toString() {
        String cookieString = "";
        for (int i = 0; i < cookiesList.size(); i++) {
            cookieString += cookiesList.get(0).toString() + ((i != cookiesList.size() - 1) ? "|" : "");
        }

        return this.method + "|" + this.url + "|" + TextUtils.join("&", postParam) + "|" + cookieString;
    }
}
