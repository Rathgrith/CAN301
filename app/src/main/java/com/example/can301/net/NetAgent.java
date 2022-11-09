package com.example.can301.net;

public interface NetAgent {
    void onSuccess(String result);
    void onError(Exception e);

}
