package net.dgistudio.guillaume.webbysms;

import org.apache.http.cookie.Cookie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 15/05/2016.
 */
public class RequestResponse {
    public String dataHtml;
    public List<Cookie> cookies;

    public RequestResponse() {
        dataHtml = "";
        cookies = new ArrayList<>();
    }
}
