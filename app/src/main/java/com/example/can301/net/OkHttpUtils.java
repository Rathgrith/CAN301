package com.example.can301.net;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtils {

    private static OkHttpUtils soleInstance = new OkHttpUtils();
    private OkHttpClient okHttpClient;
    private OkHttpUtils(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    };

    public static OkHttpUtils getSoleInstance(){
        return soleInstance;
    }


    public void doGet(String url, NetAgent netAgent, Activity activity){
        Request request = new Request
                .Builder()
                .get()
                .url(url)
                .build();

        Call call = okHttpClient.newCall(request);
        extracted(netAgent, activity, call);

    }




    public void doPostForm(String url, NetAgent netAgent, HashMap<String,String> hashMap,Activity activity){
        FormBody.Builder formBody = new FormBody.Builder();
        if (hashMap!=null){
            for(String param:hashMap.keySet()){
                formBody.add(param, hashMap.get(param));
            }
        }
        Request request = new Request
                .Builder()
                .post(formBody.build())
                .url(url)
                .build();

        Call call = okHttpClient.newCall(request);
        extracted(netAgent, activity, call);
    }



    private void extracted(NetAgent netAgent, Activity activity, Call call) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        netAgent.onError(e);
                    }
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = null;
                string = response.body().string();
                String finalString = string;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        netAgent.onSuccess(finalString);
                    }
                });

            }
        });
    }
}
