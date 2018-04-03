package android.connectify.com.connectify;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by yakov on 3/31/2018.
 */

class API extends AsyncTask<String,Void,Void> {
    InputStream inputStream;
    HttpURLConnection urlConnection;
    byte[] outputBytes;
    String query;
    String ResponseData = null;
    int code;
    apiActivity activity;
    boolean isPost;

    public API(String query, int code, apiActivity activity, boolean isPost) {
        this.query = query;
        this.code = code;
        this.activity = activity;
        this.isPost = isPost;
    }

    @Override
    protected Void doInBackground(String... params) {
        // Send data
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            if (this.isPost) {
            /* pass post data */
                outputBytes = query.getBytes("UTF-8");

                urlConnection.setRequestMethod("POST");
            }

            urlConnection.connect();
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputBytes);
            os.close();

            /* Get Response and execute WebService request*/
            int statusCode = urlConnection.getResponseCode();

            /* 200 represents HTTP OK */
            if (statusCode == HttpsURLConnection.HTTP_OK) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                this.ResponseData = convertStreamToString(inputStream);
            } else {
                this.ResponseData = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String getResponseData() {
        return ResponseData;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        this.activity.apiCallback(this.code, this.getResponseData());
        super.onPostExecute(aVoid);
    }
}