package net.dgistudio.guillaume.webbysms;

import android.util.Log;

import org.apache.http.cookie.Cookie;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Guillaume on 15/05/2016.
 */
public class CookieBuilder implements Cookie {
    private String name;
    private String value;
    private String comment;
    private String commentUrl;
    private Date expiryDate;
    private boolean isPersistent;
    private String domain;
    private String path = "/";
    private boolean isSecure;
    private int[] ports;

    public static String cookieToString(Cookie cookie) {
        return cookie.getName() + "=" + cookie.getValue() + "; " +
                "expires=" + cookie.getExpiryDate() + "; " +
                "domain=" + cookie.getDomain() + "; " +
                "path=" + cookie.getPath();
    }

    public static CookieBuilder parseString(String cookieString) {
        String[] cookiePart = cookieString.split(";");

        String TAG = "COOKIE_BUILD";

        Log.d(TAG, "parseString: " + Arrays.toString(cookiePart));

        CookieBuilder cookie = new CookieBuilder();

        for (int i = 0; i < cookiePart.length; i++) {
            if (i == 0) {
                cookie.setName(cookiePart[i].substring(0, cookiePart[i].indexOf("=")));
                cookie.setValue(cookiePart[i].substring(cookiePart[i].indexOf('=') + 1));
            } else {
                String value = cookiePart[i].substring(cookiePart[i].indexOf('=') + 1);

                Log.d(TAG, "parseString: value " + value);
                Log.d(TAG, "parseString: tocken " + cookiePart[i].substring(0, cookiePart[i].indexOf('=')));
                switch (cookiePart[i].substring(0, cookiePart[i].indexOf('=')).replace(" ", "")) {
                    case "expires":
                        cookie.setExpiryDate(new Date(value));
                        break;
                    case "domain":
                        cookie.setDomain(value);
                        break;
                    case "path":
                        cookie.setPath(value);
                        break;
                }
            }

        }
        return cookie;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getCommentURL() {
        return commentUrl;
    }

    @Override
    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean isPersistent() {
        return isPersistent;
    }

    public void setPersistent(boolean persistent) {
        isPersistent = persistent;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int[] getPorts() {
        return ports;
    }

    public void setPorts(int[] ports) {
        this.ports = ports;
    }

    @Override
    public boolean isSecure() {
        return isSecure;
    }

    public void setSecure(boolean secure) {
        isSecure = secure;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public boolean isExpired(Date date) {
        return false;
    }

    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }
}
