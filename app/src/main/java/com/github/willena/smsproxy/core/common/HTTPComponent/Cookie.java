package com.github.willena.smsproxy.core.common.HTTPComponent;

import android.util.Log;

import java.util.Date;

/**
 * Created by Guillaume on 09/07/2017.
 */

public class Cookie {
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

    @Override
    public String toString() {
        return getName() + "=" + getValue() + "; " +
                "expires=" + getExpiryDate() + "; " +
                "domain=" + getDomain() + "; " +
                "path=" + getPath();
    }

    public static Cookie parseString(String cookieString) {
        String[] cookiePart = cookieString.split(";");


        Cookie cookie = new Cookie();

        for (int i = 0; i < cookiePart.length; i++) {
            if (i == 0) {
                cookie.setName(cookiePart[i].substring(0, cookiePart[i].indexOf("=")));
                cookie.setValue(cookiePart[i].substring(cookiePart[i].indexOf('=') + 1));
            } else {
                String value = cookiePart[i].substring(cookiePart[i].indexOf('=') + 1);

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentURL() {
        return commentUrl;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isPersistent() {
        return isPersistent;
    }

    public void setPersistent(boolean persistent) {
        isPersistent = persistent;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int[] getPorts() {
        return ports;
    }

    public void setPorts(int[] ports) {
        this.ports = ports;
    }

    public boolean isSecure() {
        return isSecure;
    }

    public void setSecure(boolean secure) {
        isSecure = secure;
    }

    public int getVersion() {
        return 0;
    }

    public boolean isExpired(Date date) {
        return false;
    }

    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }
}
