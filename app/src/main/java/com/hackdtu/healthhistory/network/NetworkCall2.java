package com.hackdtu.healthhistory.network;

import java.io.IOException;
import java.net.Authenticator;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by dell on 2/11/2017.
 */

public class NetworkCall2 {
    //public static final MediaType JSON
      //      = MediaType.parse("application/json; charset=utf-8");

    /*
    OkHttpClient client = new OkHttpClient();

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        //String credential = Credentials.basic("sahil", "test12345");
        Request request = new Request.Builder()//.header("Authorization", credential)
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }*/
    OkHttpClient client = new OkHttpClient();

    public String run(String url) throws IOException {
        String credential = Credentials.basic("sahil", "test12345");
        Request request = new Request.Builder().header("Authorization",credential)
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
