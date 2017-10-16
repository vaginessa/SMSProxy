package net.dgistudio.guillaume.webbysms;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

class PageDownloader extends AsyncTask<String, Void, RequestResponse> {


    public AsyncResponse delegate = null;

    public PageDownloader(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected RequestResponse doInBackground(String... urls) {

        RequestResponse requestResponse = new RequestResponse();


        try {

            //------------------>>
            //
            BasicCookieStore cookieStore = new BasicCookieStore();

            // Create local HTTP context
            HttpContext localContext = new BasicHttpContext();
            // Bind custom cookie store to the local context
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

            HttpGet httpGet = new HttpGet(urls[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpGet, localContext);


            // StatusLine stat = response.getStatusLine();
            //int status = response.getStatusLine().getStatusCode();

            Header[] header = response.getAllHeaders();

            for (Header header1 : header) {
                Log.d("HEADER", "doInBackground: HEader : -- " + header1.toString());
            }

            requestResponse.cookies = cookieStore.getCookies();

            //if (status == 200) {
            HttpEntity entity = response.getEntity();

            requestResponse.dataHtml = EntityUtils.toString(entity);

            return requestResponse;
            //}


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(RequestResponse result) {
        delegate.processFinish(result);
    }

}
