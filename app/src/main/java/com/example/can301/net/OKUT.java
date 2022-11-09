package com.example.can301.net;

import android.app.DownloadManager;
import android.view.textclassifier.TextLinks;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OKUT {
    private OKUT(){}
    private static OKUT instance = new OKUT();
    private OkHttpClient okHttpClient = new OkHttpClient();

    public static OKUT getInstance() {
        return instance;
    }
    public String doGet(String url) throws Exception{
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        return response.body().string();

    }
}
